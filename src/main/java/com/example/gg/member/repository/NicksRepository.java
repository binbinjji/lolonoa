package com.example.gg.member.repository;

import com.example.gg.member.domain.model.Member;
import com.example.gg.member.domain.model.Nicks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicksRepository extends JpaRepository<Nicks, Long> {

    Nicks findByNick(String nick);
}
