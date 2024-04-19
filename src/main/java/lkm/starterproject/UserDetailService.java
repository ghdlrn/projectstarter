package lkm.starterproject;

import com.querydsl.core.types.dsl.BooleanExpression;
import lkm.starterproject.repository.Entity.QUserEntity;
import lkm.starterproject.repository.Entity.UserEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    //userDetailsService를 구현하여 사용자 이메일을 받아 UserDetails 로드
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QUserEntity qUser = QUserEntity.userEntity;
        BooleanExpression emailPredicate = qUser.email.eq(username);

        UserEntity user = userRepository.findOne(emailPredicate).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + username));

        return new UserCredential(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
