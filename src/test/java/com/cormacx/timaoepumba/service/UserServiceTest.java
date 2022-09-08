package com.cormacx.timaoepumba.service;


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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        when(userRepository.save(any(UserEntity.class))).then(new Answer<UserEntity>() {
            @Override
            public UserEntity answer(InvocationOnMock invocation) throws Throwable {
                UserEntity user = (UserEntity) invocation.getArgument(0);
                user.setId(UUID.randomUUID());
                return user;
            }
        });
    }

    @Test
    public void canSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("garboglargh@test.com");
        userDTO.setPassword("gargoblorg");

        Optional<UserEntity> savedUser = userService.createNewUser(userDTO);

        assertNotNull(savedUser.get());
        assertEquals("garboglargh@test.com", savedUser.get().getEmail());
        assertNotNull(savedUser.get().getId());
        assertEquals(0D, savedUser.get().getAccount().getBalance());
        //assertEquals(new ArrayList<>(), savedUser.get().getRoles());
    }

}
