package lkm.starterproject.repository;

import lkm.starterproject.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<MemberEntity, Long>, QuerydslPredicateExecutor<MemberEntity> {

    Boolean existsByEmail(String email);

    MemberEntity findByEmail(String email);
}
