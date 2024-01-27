package ec.controller;

import ec.service.UserAndRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final UserAndRoleService userAndRoleService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/v1/public/logout")
    public String test5() {
        SecurityContextHolder.clearContext();
        return "test";
    }

    @GetMapping("/v1/secured/admin")
    public String test1() {
        return "authenticated";
    }


}


