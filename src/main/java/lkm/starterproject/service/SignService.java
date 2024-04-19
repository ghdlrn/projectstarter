package lkm.starterproject.service;

import lkm.starterproject.constants.Role;
import lkm.starterproject.dto.MemberDto;
import lkm.starterproject.entity.MemberEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SignService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public SignService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(MemberDto memberDto) {
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();
        String username = memberDto.getUsername();

        Boolean isMemberExist = userRepository.existsByEmail(email);      //중복되는 email값 있는지 확인

        if (isMemberExist) {

            return;
        }
        MemberEntity memberEntity = new MemberEntity().builder()    // isMemberExist로 동일한 email이 없으면 해당 유저정보 저장
                .username(username)
                .password(passwordEncoder.encode(password))     //비밀번호 암호화해서 저장
                .email(email)
                .role(Role.USER)    //회원가입대상자는 모두 권한 USER
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(memberEntity);
    }
}
