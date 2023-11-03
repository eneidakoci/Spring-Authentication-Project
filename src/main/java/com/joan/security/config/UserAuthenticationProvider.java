package com.joan.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joan.security.dto.CredentialsDTO;
import com.joan.security.dto.User;
import com.joan.security.service.AuthenticationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class UserAuthenticationProvider {

    private static final String ID = "id";
    private static final String ROLES = "roles";
    private static final String ROLES_DELIMITER = ",";
    private final AuthenticationService authenticationService;
    @Value(
            "${security.jwt.token.secret-key}"
    )
    private String secretKey;

    public UserAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User user) throws ParseException {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer(user.getLogin())
                .withClaim(ID, user.getId())
                .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(ROLES_DELIMITER)))
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        User user = new User(); //authenticationService.findByLogin(decodedJWT.getIssuer());
        // avoided - to avoid possible DB hit if it were a prod application
        user.setId(Long.valueOf(decodedJWT.getClaim(ID).toString()));
        user.setLogin(decodedJWT.getIssuer());
        user.setAuthorities(decodedJWT.getClaim(ROLES).toString().replaceAll("\"", ""));
        user.setToken(token);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public Authentication validateCredentials(CredentialsDTO credentialsDTO) {
        User user = authenticationService.authenticate(credentialsDTO);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
