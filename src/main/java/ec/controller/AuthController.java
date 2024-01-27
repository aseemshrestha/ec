package ec.controller;

import ec.model.JwtAuthenticationResponse;
import ec.model.LoginDto;
import ec.model.User;
import ec.security.JwtService;
import ec.security.RefreshTokenRequest;
import ec.service.UserAndRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserAndRoleService userAndRoleService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

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
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(accessToken);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("access-token", "Bearer " + accessToken);
            responseHeaders.set("refresh-token", refreshTokenRequest.getToken());

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("Refresh Token Created");

        }
        return null;


    }

}
