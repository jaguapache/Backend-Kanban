package com.example.springboot.kanban.kanban_app.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.springboot.kanban.kanban_app.dto.WeatherDTO;
import com.example.springboot.kanban.kanban_app.models.User;

import com.example.springboot.kanban.kanban_app.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    public UserService(UserRepository userRepository, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder
                .baseUrl("https://api.openweathermap.org")
                .build();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setLastname(updatedUser.getLastname());
            user.setEmail(updatedUser.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }

    public WeatherDTO getWeatherByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        Integer zipCode = user.getUbication();

        if (zipCode == null || (zipCode < 1000 && zipCode > 99999)) {
            throw new IllegalArgumentException("Código postal inválido para el usuario con id: " + id);
        }

        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("zip", zipCode + ",es")
                            .queryParam("appid", openWeatherApiKey)
                            .queryParam("units", "metric")
                            .queryParam("lang", "es")
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            Object mainObj = response.get("main");

            Map<?, ?> main = (Map<?, ?>) mainObj;

            Map<String, Integer> formattedValues = formatValues(main);

            return new WeatherDTO(formattedValues.get("temp"), formattedValues.get("temp_min"),
                    formattedValues.get("temp_max"));

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error de respuesta al llamar a la API de OpenWeather" + e.getStatusCode() + ": "
                    + e.getResponseBodyAsString());
        }
    }

    private Map<String, Integer> formatValues(Map<?, ?> main) {
        Double tempDouble = Double.parseDouble(main.get("temp").toString());
        Double tempMinDouble = Double.parseDouble(main.get("temp_min").toString());
        Double tempMaxDouble = Double.parseDouble(main.get("temp_max").toString());

        Integer temp = (int) Math.round(tempDouble);
        Integer tempMin = (int) Math.round(tempMinDouble);
        Integer tempMax = (int) Math.round(tempMaxDouble);

        return Map.of("temp", temp, "temp_min", tempMin, "temp_max", tempMax);
    }

}
