package com.example.springboot.kanban.kanban_app.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.kanban.kanban_app.dto.WeatherDTO;
import com.example.springboot.kanban.kanban_app.models.User;

import com.example.springboot.kanban.kanban_app.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    private String formatError(RuntimeException e) {
        if (e.getMessage().contains("404"))
            return "Ciudad no encontrada para el código postal proporcionado";
        if (e.getMessage().contains("400"))
            return "Código postal inválido";
        return "Error al obtener la información meteorológica: " + e.getMessage();
    }

    @PostMapping("createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getLastname() == null || user.getEmail() == null
                || user.getUbication() == null
                || user.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Los campos nombre, apellido, email y ubication no pueden estar vacíos o nulos");
        }
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        user.setAdmin(false);
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(users.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(users);
    }

    @GetMapping("getUsersActivated")
    public ResponseEntity<List<User>> getUsersActivated() {
        List<User> users = userService.getUsersActivated();
        return ResponseEntity.status(users.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(users);
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (user.getName() == null || user.getLastname() == null || user.getEmail() == null
                || user.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre no puede estar vacío o nulo");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        boolean hasWriteScope = authentication.getAuthorities().stream()
                .anyMatch(a -> "SCOPE_users.write".equals(a.getAuthority()));

        User loggedUser = userService.getUserByEmail(authentication.getName());

        boolean isOwner = loggedUser != null && loggedUser.getId() != null && loggedUser.getId().equals(id);

        if (!hasWriteScope && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permisos para modificar este usuario");
        }

        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado con id: " + id);
        }
    }

    @GetMapping("getWeather/{id}")
    public ResponseEntity<?> getWeather(@PathVariable Long id) {
        try {
            WeatherDTO weather = userService.getWeatherByUserId(id);
            return ResponseEntity.ok(weather);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(formatError(e));
        }
    }

    @GetMapping("getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
            }
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> loginMethod(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El email no puede estar vacío o nulo");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La contraseña no puede estar vacía o nula");
        }

        try {
            User dbUser = userService.getUserByEmail(user.getEmail());
            if (dbUser == null || !dbUser.isEnabled()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Instant now = Instant.now();

            boolean hasWriteRole = authentication.getAuthorities().stream()
                    .anyMatch(a -> "WRITE".equals(a.getAuthority()));

            String scope = hasWriteRole ? "users.read users.write" : "users.read";

            Long userId = dbUser.getId();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(3600))
                    .subject(user.getEmail())
                    .claim("user_id", userId)
                    .claim("scope", scope)
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            Map<String, Object> body = new HashMap<>();
            body.put("access_token", token);
            body.put("token_type", "Bearer");
            body.put("scope", scope);

            return ResponseEntity.ok(body);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos");
        }
    }

}
