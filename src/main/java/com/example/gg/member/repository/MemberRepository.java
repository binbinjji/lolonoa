package com.example.gg.member.repository;

import com.example.gg.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);
    boolean existsByAccount(String account);

}
