//package lkm.starterproject.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lkm.starterproject.constants.Role;
//import lkm.starterproject.dto.CustomUserDetails;
//import lkm.starterproject.entity.MemberEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//    public JWTFilter(JWTUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter) throws ServletException, IOException {
//
//        String authorization= req.getHeader("Authorization");
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {    //헤더값이 null이거나 Bearer 가 포함되어있지않으면 종료
//            System.out.println("token null");
//            filter.doFilter(req, res);
//            return;
//        }
//
//        String token = authorization.substring(7);      //Bearer 이후 문자열(공백포함) 순수 토큰만 추출
//
//        if (jwtUtil.isExpired(token)) {     //토큰 유효한지 확인
//            System.out.println("token expired");
//            filter.doFilter(req, res);
//            return;     //조건 만족하면 메소드 종료
//        }
//
//        String email = jwtUtil.getEmail(token);     //토큰에서 email, role 획득(JWT는 디코딩도 쉬워서 비밀번호는 담지 않음)
//        String role = jwtUtil.getRole(token);
//
//        MemberEntity memberEntity = new MemberEntity().builder()    // isMemberExist로 동일한 email이 없으면 해당 유저정보 저장, 토큰에서 얻은 값 set
//                .username(email)
//                .password("temppassword") //임의비밀번호(비밀번호는 토큰에 담지 않았는데 DB에 조회하면 부담이 가 지장없는 임의 비밀번호 설정)
//                .role(Role.valueOf(role))
//                .build();
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(memberEntity);  //UserDetails에 회원정보 객체담기
//
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authToken);    //Stateless 세션 생성
//
//        filter.doFilter(req, res);
//    }
//}
