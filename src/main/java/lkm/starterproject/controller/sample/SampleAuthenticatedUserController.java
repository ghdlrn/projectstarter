package lkm.starterproject.controller.sample;

import lkm.starterproject.CustomUserCredential;
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
public class SampleAuthenticatedUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SampleAuthenticatedUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public UserViewModel getLoggedInUser(@AuthenticationPrincipal CustomUserCredential userCredential) {
        UserEntity userEntity = this.userRepository.findById(userCredential.getUserId()).orElseThrow(() -> new RuntimeException("오류"));
        return new UserViewModel(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    @PostMapping
    public void addUser(@RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));

        userRepository.save(userEntity);
    }

    @PutMapping
    public void updateUser(@AuthenticationPrincipal CustomUserCredential userCredential, @RequestBody UserDto userDto) {
        var userEntity = new UserEntity();
        userEntity.setUserId(userCredential.getUserId());
        userEntity.setName(userDto.name());
        userEntity.setEmail(userDto.email());
        userEntity.setPassword(this.passwordEncoder.encode(userDto.password()));

        userRepository.save(userEntity);
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal CustomUserCredential userCredential) {
        userRepository.deleteById(userCredential.getUserId());
    }

    public record UserViewModel(Long userId, String name, String email) {
    }
}
