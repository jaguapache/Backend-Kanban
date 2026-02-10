package com.example.springboot.kanban.kanban_app.validation;

import org.springframework.stereotype.Component;

import com.example.springboot.kanban.kanban_app.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {
    private final UserService userService;

    public ExistsByUsernameValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userService.existsByEmail(value);
    }

}
