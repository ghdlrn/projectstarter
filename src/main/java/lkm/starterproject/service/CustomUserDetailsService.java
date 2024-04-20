package lkm.starterproject.service;

import lkm.starterproject.dto.CustomUserDetails;
import lkm.starterproject.entity.MemberEntity;
import lkm.starterproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MemberEntity memberEntity = userRepository.findByEmail(email);   //DB에서 이메일 찾음

        if (memberEntity != null) {
            return new CustomUserDetails(memberEntity);
        }

        return null;
    }
}
