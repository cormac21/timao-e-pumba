package com.cormacx.timaoepumba.service;


import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.user.Role;
import com.cormacx.timaoepumba.entities.user.UserDTO;
import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.repositories.RoleRepository;
import com.cormacx.timaoepumba.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void init() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        when(userRepository.save(any(UserEntity.class))).then((Answer<UserEntity>) invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        Account account = new Account();
        account.setUserUUID("some-fake-userr-uuid");
        account.setBalance(0D);
        account.setActive(true);

        when(accountService.createNewAccount(anyString())).thenReturn(account);
    }

    @Test
    public void canSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("garboglargh@test.com");
        userDTO.setPassword("gargoblorg");

        Optional<UserEntity> savedUser = userService.createNewUser(userDTO);

        assertTrue(savedUser.isPresent());
        assertNotNull(savedUser.get());
        assertEquals("garboglargh@test.com", savedUser.get().getEmail());
        assertNotNull(savedUser.get().getId());
    }

    @Test
    public void canUpdateUserWithNewInformation() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("garboglargh@test.com");
        userDTO.setPassword("gargoblorg");
        Optional<UserEntity> newUser = userService.createNewUser(userDTO);
        newUser.get().setUsername("Um nome muito bacana pra esse usuario");

        UserEntity updatedUser = userService.saveOrUpdateUser(newUser.get());

        verify(userRepository, atLeast(2)).save(newUser.get());
        assertEquals("Um nome muito bacana pra esse usuario", updatedUser.getUsername());
        assertEquals("garboglargh@test.com", updatedUser.getEmail());
        assertEquals(newUser.get().getId(), updatedUser.getId());
    }

}
