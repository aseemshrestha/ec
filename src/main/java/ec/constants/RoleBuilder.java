package ec.constants;

import ec.model.Role;

public class RoleBuilder {
    private static final Role siteUser;

    private static final Role admin;


    static {
        siteUser = Role.builder()
                .id(Roles.SITE_USER_I.getValue())
                .role(Roles.SITE_USER_I.name())
                .build();

        admin = Role.builder()
                .id(Roles.ADMIN.getValue())
                .role(Roles.ADMIN.name())
                .build();
    }

    public static Role getRoleAdmin() {
        return admin;
    }

    public static Role getSiteUser() {
        return siteUser;
    }


}
