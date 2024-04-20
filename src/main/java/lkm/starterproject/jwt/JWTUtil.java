package lkm.starterproject.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {   //email토큰 검증 메서드
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
        //토큰이 내 서버에서 생성되었는지   생성된 키가 내가 가진 secretKet와 일치하는지 확인      email을 String으로 가져옴
    }

    public String getRole(String token) {       //role(권한) 토큰 검증 메서드
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {      //토큰 만료 확인 검증 메서드 소멸되면 true, 아직 남아있으면 false
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String email, String role, Long expiredMs) { //토큰 생성 메서드

        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))     //토큰 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs))   //토큰 소멸시간
                .signWith(secretKey)    //토큰 암호화
                .compact();
    }
}
