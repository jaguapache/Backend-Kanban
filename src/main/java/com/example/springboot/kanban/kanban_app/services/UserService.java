package com.example.springboot.kanban.kanban_app.services;

import java.util.List;

import com.example.springboot.kanban.kanban_app.dto.WeatherDTO;
import com.example.springboot.kanban.kanban_app.models.User;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    WeatherDTO getWeatherByUserId(Long id);

    boolean existsByEmail(String email);

}
