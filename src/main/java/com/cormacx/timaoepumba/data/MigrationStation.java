package com.cormacx.timaoepumba.data;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.order.OrderDTO;
import com.cormacx.timaoepumba.entities.user.Privilege;
import com.cormacx.timaoepumba.entities.user.Role;
import com.cormacx.timaoepumba.entities.user.UserDTO;
import com.cormacx.timaoepumba.entities.user.UserEntity;
import com.cormacx.timaoepumba.repositories.PrivilegeRepository;
import com.cormacx.timaoepumba.repositories.RoleRepository;
import com.cormacx.timaoepumba.service.AccountService;
import com.cormacx.timaoepumba.service.OrderService;
import com.cormacx.timaoepumba.service.UserService;
import java.util.Optional;
import java.util.Set;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MigrationStation implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(MigrationStation.class);

    private boolean alreadySetup = false;

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final OrderService orderService;

    public String genericGuyId;

    @Autowired
    public MigrationStation(UserService userService, RoleRepository roleRepository,
                            PrivilegeRepository privilegeRepository,
                            PasswordEncoder passwordEncoder,
                            AccountService accountService,
                            OrderService orderService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.orderService = orderService;
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
        UserEntity genericGuy = createGenericUserIfNotFound();
        genericGuyId = genericGuy.getId().toString();
        Optional<Account> acc = accountService.findAccountByUser(genericGuyId);
        if(acc.isPresent()) {
            accountService.addFundsToAccount(acc.get(), 5000D);
        }
        createSomeStartingOrders();

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
    void createAdminIfNotFound(Role adminRole) {
        var userOp = userService.findUserByEmail("admin@timaoepumba.com.br");
        if (userOp.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("TimaoEPumba Administrator");
            user.setEmail("admin@timaoepumba.com.br");
            user.setPassword(passwordEncoder.encode("admin1234"));
            user.setRoles(Set.of(adminRole));
            userService.saveOrUpdateUser(user);
        }
    }

    private UserEntity createGenericUserIfNotFound() {
        String bogusEmail = "test@timaoepumba.com";
        Optional<UserEntity> userOp = userService.findUserByEmail(bogusEmail);
        if (userOp.isEmpty()) {
            UserDTO newUser = new UserDTO();
            newUser.setEmail(bogusEmail);
            newUser.setPassword("timaoEPumba");
            Optional<UserEntity> saved = userService.createNewUser(newUser);
            if(saved.isPresent()){
                log.info("User saved: ".concat(saved.get().getId().toString()));
                return saved.get();
            }
        }
        return null;
    }

    private void createSomeStartingOrders() {
        OrderDTO firstBuyOrder = new OrderDTO();
        firstBuyOrder.setUserUUID(genericGuyId);
        firstBuyOrder.setTicker("MGLU3");
        firstBuyOrder.setQuantity(300);
        firstBuyOrder.setUnitPrice(4.5D);
        firstBuyOrder.setType("c");

        OrderDTO firstSellOrder = new OrderDTO();
        firstSellOrder.setUserUUID(genericGuyId);
        firstSellOrder.setTicker("MGLU3");
        firstSellOrder.setQuantity(300);
        firstSellOrder.setUnitPrice(4.3D);
        firstSellOrder.setType("v");

        OrderDTO secondBuyOrder = new OrderDTO();
        secondBuyOrder.setUserUUID(genericGuyId);
        secondBuyOrder.setTicker("MGLU3");
        secondBuyOrder.setQuantity(100);
        secondBuyOrder.setUnitPrice(4.7D);
        secondBuyOrder.setType("c");

        orderService.createNewOrder(firstBuyOrder);
        orderService.createNewOrder(firstSellOrder);
        orderService.createNewOrder(secondBuyOrder);
    }

}
