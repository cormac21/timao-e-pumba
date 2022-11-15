package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.entities.user.UserDTO;
import com.cormacx.timaoepumba.security.JWT;
import com.cormacx.timaoepumba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", exposedHeaders = "*")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authManager) {
        this.userService = userService;
        this.authenticationManager = authManager;
    }

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody @Validated UserDTO user) {
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> userOp = userService.createNewUser(user);
        if (userOp.isPresent()) {
            final var location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userOp.get().getId())
                    .toUri();
            return ResponseEntity.created(location).body(userOp.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> userEntities = userService.findAll();
        return new ResponseEntity<>(userEntities, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(),
                    userDTO.getPassword()));
            final var user = userService.findUserByEmail(userDTO.getEmail());
            String token = JWT.token(user.get());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token).body(user.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
