package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.entities.user.UserDTO;
import com.cormacx.timaoepumba.repositories.RoleRepository;
import com.cormacx.timaoepumba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> createNewUser(UserDTO user) {
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(newUserEntity.getEmail());
        newUserEntity.setPassword(newUserEntity.getPassword());
        newUserEntity.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));

        return Optional.of(userRepository.save(newUserEntity));
    }
}
