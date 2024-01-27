package ec.controller;

import ec.constants.AppConstants;
import ec.constants.MailType;
import ec.constants.RoleBuilder;
import ec.constants.UserStatus;
import ec.model.ErrorMessage;
import ec.model.LoginDto;
import ec.model.Mail;
import ec.model.Maintainer;
import ec.model.User;
import ec.repository.MaintainerRepository;
import ec.security.JwtService;
import ec.security.RefreshTokenRequest;
import ec.service.EmailService;
import ec.service.UserAndRoleService;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AuthController {
    private final UserAndRoleService userAndRoleService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final MaintainerRepository maintainerRepository;


    @PostMapping("/v1/public/login")
    public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        var user = userAndRoleService.getUser(loginDto.getUsername());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }
        var accessToken = jwtService.generateToken(user.get().getEmail());
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user.get().getEmail());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("access-token", "Bearer " + accessToken);
        responseHeaders.set("refresh-token", refreshToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("SignIn Successful");
    }


    @PostMapping("v1/public/token-refresh")
    public ResponseEntity<String> signIn(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        Optional<User> user = userAndRoleService.getUser(userEmail);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user.get().getEmail())) {
            var accessToken = jwtService.generateToken(user.get().getEmail());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("access-token", "Bearer " + accessToken);
            responseHeaders.set("refresh-token", refreshTokenRequest.getToken());

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("Refresh Token Created");

        }
        return null;


    }

    @PostMapping("v1/public/create-user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, HttpServletRequest request) throws InterruptedException {

        List<Maintainer> maintainers = maintainerRepository.findAll();
        boolean isAllowed = false;
        for (Maintainer m : maintainers) {
            if (m.getEmail().equals(user.getEmail())) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            log.info("email is not whitelisted to register: " + user.getEmail());
            ErrorMessage errorMessage = new ErrorMessage("Not enough permission to register", HttpStatus.BAD_REQUEST.name());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Optional<User> userExisting = userAndRoleService.getUser(user.getEmail());

        if (userExisting.isPresent() && userExisting.get().getEmail().equals(user.getEmail())) {
            log.info("email already exists: " + user.getEmail());
            ErrorMessage errorMessage = new ErrorMessage("Email already exists", HttpStatus.BAD_REQUEST.name());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }


        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setIp(ip);
        user.setLastUpdated(new Date());
        user.setUsername(user.getEmail());
        user.setBrowser(userAgent.getBrowser().getName() + "-" + userAgent.getOperatingSystem());
        user.setRole(RoleBuilder.getRoleAdmin());
        user.setCreated(new Date());
        user.setIsActive(UserStatus.ACTIVE.get());

        this.userAndRoleService.saveUser(user);

        log.info("User successfully created:" + "name:" + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail());
        Mail mail =
                Mail.builder().toEmail(user.getEmail()).subject(AppConstants.Welcome.WELCOME_PASS_SUBJECT)
                        .name(user.getFirstName())
                        .username(user.getUsername())
                        .message(AppConstants.Welcome.WELCOME_MESSAGE).build();

        emailService.sendMail(mail, MailType.WELCOME);
        return new ResponseEntity<>("You have successfully registered. You may now login.", HttpStatus.CREATED);
    }

}
