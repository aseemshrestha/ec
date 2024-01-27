package ec.util;

import ec.constants.RoleBuilder;
import ec.model.Maintainer;
import ec.model.Role;
import ec.repository.MaintainerRepository;
import ec.service.UserAndRoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbSeed implements CommandLineRunner {
    private final UserAndRoleService userAndRoleService;
    private final MaintainerRepository maintainerRepository;

    public DbSeed(UserAndRoleService userAndRoleService, MaintainerRepository maintainerRepository) {
        this.userAndRoleService = userAndRoleService;
        this.maintainerRepository = maintainerRepository;

    }


    @Override
    public void run(String... args) {
        

        List<Maintainer> maintainerList = new ArrayList<>();

        Maintainer maintainer1 = new Maintainer();
        maintainer1.setEmail("sanjaykarki555@gmail.com");

        Maintainer maintainer2 = new Maintainer();
        maintainer2.setEmail("pdhungel@gmail.com ");

        Maintainer maintainer3 = new Maintainer();
        maintainer3.setEmail("shrestha.asm@gmail.com");

        maintainerList.add(maintainer1);
        maintainerList.add(maintainer2);
        maintainerList.add(maintainer3);


        List<Role> roles = new ArrayList<>();
        roles.add(RoleBuilder.getRoleAdmin());
        roles.add(RoleBuilder.getSiteUser());

        roles.forEach(r -> userAndRoleService.saveRole(r));

        maintainerRepository.saveAll(maintainerList);

    }

}