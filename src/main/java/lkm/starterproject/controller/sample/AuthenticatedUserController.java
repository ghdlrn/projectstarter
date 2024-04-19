package lkm.starterproject.controller.sample;

import lkm.starterproject.UserCredential;
import lkm.starterproject.dto.UserDto;
import lkm.starterproject.repository.Entity.UserEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class AuthenticatedUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticatedUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }   //사용자 데이터관리, 비밀번호 암호화

    @GetMapping     //인증된 사용자정보 조회(CustomUserCredential)
    public UserViewModel getLoggedInUser(@AuthenticationPrincipal UserCredential userCredential) {
        UserEntity userEntity = this.userRepository.findById(userCredential.getUserId()).orElseThrow(() -> new RuntimeException("오류"));
        return new UserViewModel(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    @PostMapping        //회원가입
    public void addUser(@RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));
            //비밀번호는 암호화해서 저장
        userRepository.save(userEntity);
    }

    @PutMapping     //사용자정보 수정
    public void updateUser(@AuthenticationPrincipal UserCredential userCredential, @RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setUserId(userCredential.getUserId());
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));

        userRepository.save(userEntity);
    }

    @DeleteMapping      //계정 삭제
    public void deleteUser(@AuthenticationPrincipal UserCredential userCredential) {
        userRepository.deleteById(userCredential.getUserId());
    }

    public record UserViewModel(Long userId, String name, String email) {
    }
}
