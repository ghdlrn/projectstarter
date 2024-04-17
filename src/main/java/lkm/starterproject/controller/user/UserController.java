package lkm.starterproject.controller.user;

import lkm.starterproject.Entity.UserEntity;
import lkm.starterproject.custom.CustomUserCredential;
import lkm.starterproject.dto.UserDto;
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

@RequestMapping("/api")
@RestController
public class UserController {        //인증된 사용자 http 요청 처리

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")     //로그인한 사용자 ID확인하여 UserRepository에서 사용자정보 조회
    public UserViewModel getLoggedInUser(@AuthenticationPrincipal CustomUserCredential userCredential) {
        UserEntity userEntity = this.userRepository.findById(userCredential.getUserId()).orElseThrow(() -> new RuntimeException(""));
        return new UserViewModel(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    @PostMapping("register")        //회원가입
    public void addUser(@RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));

        userRepository.save(userEntity);
    }

    @PutMapping("/users")     //사용자정보 수정
    public void updateUser(@AuthenticationPrincipal CustomUserCredential userCredential, @RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setUserId(userCredential.getUserId());
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));

        userRepository.save(userEntity);
    }

    @DeleteMapping("/users")      //사용자정보 삭제
    public void deleteUser(@AuthenticationPrincipal CustomUserCredential userCredential) {
        userRepository.deleteById(userCredential.getUserId());
    }

    public record UserViewModel(Long userId, String name, String email) {
    }
}