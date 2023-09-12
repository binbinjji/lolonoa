package com.example.gg.member.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String account;

    private String password;

    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    List<Nicks> nicks = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "most_id")
    private Nicks most;


    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }

    public void add_Nicks(Nicks nick){
        nicks.add(nick);
    }

//    public void addGameName(GameName gameName){
//        this.gameNames.add(gameName);
//    }


//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
}