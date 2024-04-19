package lkm.starterproject.controller.sample;

import com.querydsl.core.types.Predicate;
import lkm.starterproject.repository.Entity.UserEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController { //사용자정보들 조회

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserEntity> getUsers(@QuerydslPredicate(root = UserEntity.class) Predicate predicate) {
        return (List<UserEntity>) userRepository.findAll(predicate);
    }
}
