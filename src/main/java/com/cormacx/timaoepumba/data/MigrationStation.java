package com.cormacx.timaoepumba.data;

import com.cormacx.timaoepumba.entities.user.Privilege;
import com.cormacx.timaoepumba.entities.user.Role;
import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.repositories.PrivilegeRepository;
import com.cormacx.timaoepumba.repositories.RoleRepository;
import com.cormacx.timaoepumba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Component
public class MigrationStation implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MigrationStation(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PrivilegeRepository privilegeRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;

        final var manage = createPrivilegeIfNotFound("MANAGE");
        final var write = createPrivilegeIfNotFound("WRITE");
        final var read = createPrivilegeIfNotFound("READ");

        final var admin = createRoleIfNotFound("ADMIN", Set.of(manage, write, read));
        createRoleIfNotFound("USER", Set.of(write, read));
        createAdminIfNotFound(admin);
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        var privilegeOp = privilegeRepository.findByName(name);
        if (privilegeOp.isEmpty()) {
            privilegeOp = Optional.of(new Privilege(name));
            privilegeRepository.save(privilegeOp.get());
        }
        return privilegeOp.get();
    }

    @Transactional
    Role createRoleIfNotFound(String name, Set<Privilege> privileges) {
        var roleOp = roleRepository.findByName(name);
        if (roleOp.isEmpty()) {
            roleOp = Optional.of(new Role(name));
            roleOp.get().setPrivileges(privileges);
            roleRepository.save(roleOp.get());
        }
        return roleOp.get();
    }

    @Transactional
    UserEntity createAdminIfNotFound(Role adminRole) {
        var userOp = userRepository.findByEmail("admin@timaoepumba.com.br");
        if (userOp.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("TimaoEPumba Administrator");
            user.setEmail("admin@timaoepumba.com.br");
            user.setPassword(passwordEncoder.encode("admin1234"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
        }
        return userOp.get();
    }

}
