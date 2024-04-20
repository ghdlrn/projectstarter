package lkm.starterproject.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lkm.starterproject.dto.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override       //인증메서드
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        //클라이언트요청에서 email, password 추출
        String email = obtainEmail(req);
        String password = obtainPassword(req);
        //email, password, role을 토큰에 담음
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        //토큰을 AuthenticationManger로 전달
        return authenticationManager.authenticate(authToken);
    }

    protected String obtainEmail(HttpServletRequest request) {  //username이 아닌 email로 검증하기 위해 정의
        return request.getParameter("email");
    }

    @Override       //인증성공했을시
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getUsername();     //CustomUserDetails의 get.Username()을통해 email정보 가져옴

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();  //role값 받아옴

        String token = jwtUtil.createJwt(email, role, 60*60*10L);       //토큰에 email, role, 토큰 만료시간 담음

        res.addHeader("Authorization", "Bearer " + token);      //헤더에  RFC7235정의에따라 Http인증방식 지정
    }

    @Override       //인증 실패시
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {
        res.setStatus(401);
    }
}