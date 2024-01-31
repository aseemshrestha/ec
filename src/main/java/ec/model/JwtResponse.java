package ec.model;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;

    private String firstName;
}
