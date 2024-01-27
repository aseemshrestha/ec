package ec.util;

import ec.constants.RoleBuilder;
import ec.model.Role;
import ec.service.UserAndRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbSeed implements CommandLineRunner {
    private final UserAndRoleService userAndRoleService;

    public DbSeed(UserAndRoleService userAndRoleService) {
        this.userAndRoleService = userAndRoleService;
    }


    @Override
    public void run(String... args) {
        List<Role> roles = new ArrayList<>();


        roles.add(RoleBuilder.getRoleAdmin());
        roles.add(RoleBuilder.getSiteUser());

        roles.forEach(r -> userAndRoleService.saveRole(r));


    }

}