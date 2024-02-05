package ec.controller;

import ec.constants.AppConstants;
import ec.constants.MailType;
import ec.constants.RoleBuilder;
import ec.constants.UserStatus;
import ec.model.*;
import ec.repository.MaintainerRepository;
import ec.security.JwtService;
import ec.security.RefreshTokenRequest;
import ec.service.EmailService;
import ec.service.UserAndRoleService;
import ec.util.IpAndBrowserGetter;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<JwtResponse> signIn(@RequestBody LoginDto loginDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        var user = userAndRoleService.getUser(loginDto.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }
        var accessToken = jwtService.generateToken(user.get().getEmail());
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user.get().getEmail());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setFirstName(Base64.getEncoder().encodeToString(user.get().getFirstName().getBytes()));

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
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
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setIp(IpAndBrowserGetter.getIp(request));
        user.setLastUpdated(new Date());
        user.setUsername(user.getEmail());
        user.setBrowser(IpAndBrowserGetter.getBrowserAndOs(request));
        user.setRole(RoleBuilder.getRoleAdmin());
        user.setCreated(new Date());
        user.setIsActive(UserStatus.ACTIVE.get());

        this.userAndRoleService.saveUser(user);

        log.info("User successfully created:" + "name:" + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail());
        Mail mail =
                Mail.builder().toEmail(user.getEmail())
                        .subject(AppConstants.Welcome.WELCOME_PASS_SUBJECT)
                        .name(user.getFirstName())
                        .username(user.getUsername())
                        .message(AppConstants.Welcome.WELCOME_MESSAGE).build();

        emailService.sendMail(mail, MailType.WELCOME);
        return new ResponseEntity<>("You have successfully registered. You may now login.", HttpStatus.CREATED);
    }

}
