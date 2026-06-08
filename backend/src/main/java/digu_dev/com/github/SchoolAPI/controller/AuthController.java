package digu_dev.com.github.SchoolAPI.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import digu_dev.com.github.SchoolAPI.dto.AuthenticatedUserResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public AuthenticatedUserResponse currentUser(JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("preferred_username", jwt.getClaimAsString("preferred_username"));
        claims.put("email", jwt.getClaimAsString("email"));
        claims.put("scope", jwt.getClaimAsString("scope"));
        claims.put("realm_access", jwt.getClaim("realm_access"));

        return new AuthenticatedUserResponse(
                resolveUsername(authentication, jwt),
                jwt.getSubject(),
                jwt.getIssuer() == null ? null : jwt.getIssuer().toString(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt(),
                authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .sorted()
                        .toList(),
                claims);
    }

    private String resolveUsername(Authentication authentication, Jwt jwt) {
        if (authentication.getName() != null && !authentication.getName().isBlank()) {
            return authentication.getName();
        }
        return jwt.getSubject();
    }
}
