package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.user.User;
import com.cormacx.timaoepumba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        Optional<User> userOp = userRepository.findByEmail(email);
        if (userOp.isPresent()) {
            return userOp;
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> createNewUser(User user) {
        User userOp = userRepository.save(user);
        return Optional.of(userOp);
    }
}
