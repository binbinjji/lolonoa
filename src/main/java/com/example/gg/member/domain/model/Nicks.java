package com.example.gg.member.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Nicks {

    @Id
    @Column(name = "nicks_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nick;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;

    @OneToOne(mappedBy = "most")
    private Member most;

}
