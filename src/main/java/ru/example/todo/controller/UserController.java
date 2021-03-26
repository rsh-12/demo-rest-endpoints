package ru.example.todo.controller;
/*
 * Date: 3/25/21
 * Time: 4:39 PM
 * */

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.todo.dto.UserDto;
import ru.example.todo.entity.User;
import ru.example.todo.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> login(@Valid @RequestBody UserDto userDto) {
        String tokens = userService.login(userDto);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        String register = userService.register(modelMapper.map(userDto, User.class));
        return ResponseEntity.ok(register);
    }
}