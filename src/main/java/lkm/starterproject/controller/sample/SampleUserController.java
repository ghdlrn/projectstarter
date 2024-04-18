package lkm.starterproject.controller.sample;

import lkm.starterproject.repository.Entity.UserEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("sample/users")
@RestController
public class SampleUserController {

    private final UserRepository userRepository;

    public SampleUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserEntity> getUsers() {
        return this.userRepository.findAll();
    }
}
