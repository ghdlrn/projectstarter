package lkm.starterproject.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lkm.starterproject.constants.Role;
import lkm.starterproject.dto.CustomUserDetails;
import lkm.starterproject.entity.MemberEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization= request.getHeader("Authorization");   //request에서 Authorization 헤더를 찾음

        if (authorization == null || !authorization.startsWith("Bearer ")) {    //Authorization이 null이나 접두사가 Bearer가 아니면 메서드종료
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1]; //접두사 Bearer제거한 토큰 추출
        if (jwtUtil.isExpired(token)) {     //토큰 유효 검증
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getEmail(token);  //토큰에서 email과 role 가져옴
        String role = jwtUtil.getRole(token);

        MemberEntity memberEntity = new MemberEntity().builder()    // MemberEntity 생성하여 토큰+임시비밀번호값 set
                .email(username)
                .password("123456789")   //임시비밀번호(매번 db요청하면 부담이 크고 해당 메서드에 정확한 비밀번호 필요 없음)
                .role(Role.valueOf(role))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(memberEntity);  //UserDetails에 회원 정보 객체 담기
        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}