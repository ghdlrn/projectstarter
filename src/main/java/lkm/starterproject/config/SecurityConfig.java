package lkm.starterproject.config;

import lkm.starterproject.jwt.JWTUtil;
import lkm.starterproject.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {        //비밀번호를 암호화해서 검증
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    //csrf -> disable 설정(session방식은 csrf를 필수적으로 방어해야하지만 jwt방식은 session을 stateless방식으로 관리하여 비활성화해도됨
                .csrf( (auth) -> auth.disable() );  //csrf(Cross Site Request Forgery : 사이트간 위조 요청) get을 제외한 post, put, delete요청으로부터 보호
        http
                .formLogin( (auth) -> auth.disable() ); //Form로그인 방식 비활성화
        http
                .httpBasic( (auth) -> auth.disable() ); //http basic 인증방식 비활성화
        http
                .authorizeHttpRequests( (auth) -> auth      //경로별 인가작업
                        .requestMatchers("/login", "/", "/signup").permitAll()    // 해당 경로는 모든권한 허용
                        .requestMatchers("/admin").hasRole("ADMIN")     // 해당경로는 admin 권한대상자만 사용
                        .anyRequest().authenticated());     //기타 경로는 로그인한 사용자만 사용가능
        http    //기존의 필터를 LoginFilter로 대체함, AuthenticationManager()와 JWTUtil 전달
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));       //JWT방식 인증/인가 방식은 session을 stateless방식으로 반드시 설정해야함
        return http.build();
    }
}