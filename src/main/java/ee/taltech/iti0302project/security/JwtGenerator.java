package ee.taltech.iti0302project.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {

    private final SecretKey key;

    @Autowired
    public JwtGenerator(SecretKey key) {
        this.key = key;
    }

    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .subject(authentication.getName())
//                .claims(Map.of(
//                        "userId", user.getId()
//                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key)
                .compact();
    }
}
