package com.example.gg.review;

import com.example.gg.member.domain.model.Member;
import com.example.gg.review.domain.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.nickname=:nickname")
    List<Review> findByNickname(String nickname);

    @Query("select r from Review r where r.nickname=:nickname and r.sender=:sender and r.matchId=:match_id")
    Optional<Review> findByConditions(String sender, String nickname, String match_id);

    @Query("select r from Review r left join r.member where r.member=:member order by r.id asc")
    Page<Review> findReviewsByMember(Pageable pageable, Member member);
}
