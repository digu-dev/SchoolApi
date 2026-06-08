package digu_dev.com.github.SchoolAPI.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record AuthenticatedUserResponse(
        String username,
        String subject,
        String issuer,
        Instant issuedAt,
        Instant expiresAt,
        List<String> authorities,
        Map<String, Object> claims) {
}
