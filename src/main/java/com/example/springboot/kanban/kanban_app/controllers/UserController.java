package com.example.springboot.kanban.kanban_app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.kanban.kanban_app.dto.WeatherDTO;
import com.example.springboot.kanban.kanban_app.models.User;

import com.example.springboot.kanban.kanban_app.services.UserService;

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

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(users.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(users);
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (user.getName() == null || user.getLastname() == null || user.getEmail() == null
                || user.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre no puede estar vacío o nulo");
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
}
