package lkm.starterproject.config;

import lkm.starterproject.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final UserDetailService userDetailService;  //이메일로 사용자정보 조회하고 UserDetail 객체반환

    private static final String[] PUBLIC_ENDPOINTS = {
            "/",
    };

    public SpringSecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .userDetailsService(this.userDetailService)   //customUserDetail로 로드된 사용자정보 인증
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(PUBLIC_ENDPOINTS) //Public_endpoints에 지정된 경로는 인증없이 접근 가능
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(customizer -> {
                    customizer
                            .loginProcessingUrl("/api/login")   // /api/login url을 통해 로그인 처리
                            .permitAll();
                })
                .cors((e) -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { //비밀번호 인코딩
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {     //CORS설정
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:8282");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

