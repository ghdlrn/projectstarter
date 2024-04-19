package lkm.starterproject.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkm.starterproject.repository.Entity.UserEntity;
import lkm.starterproject.repository.Entity.QUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JPAQueryFactory queryFactory;

    public List<Long> testSelect() {
        QUserEntity qUser = QUserEntity.userEntity;
        return queryFactory.select(qUser.userId)
                .from(qUser)
                .fetch();
    }

    public Optional<UserEntity> findByEmail(String email) {
        QUserEntity qUser = QUserEntity.userEntity;
        UserEntity user = queryFactory.selectFrom(qUser)
                .where(qUser.email.eq(email))
                .fetchOne();
        return Optional.ofNullable(user);
    }
}
