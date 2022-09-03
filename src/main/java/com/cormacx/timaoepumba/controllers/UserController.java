package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.entities.user.UserDTO;
import com.cormacx.timaoepumba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createNewUser(@RequestBody UserDTO user) {
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> userOp = userService.createNewUser(user);
        if (userOp.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> userEntities = userService.findAll();
        return new ResponseEntity<>(userEntities, HttpStatus.OK);
    }


}
