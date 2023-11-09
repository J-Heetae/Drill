package project.drill.domain;

import java.io.Serializable;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Builder
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(name="member_email", unique = true, nullable = false)
    private String memberEmail;

    @Column(name="member_nickname",unique = true, nullable = true)
    private String memberNickname;

    @Column(name="password")
    private String password;

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'center0'")
    private Center center;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ROLE_BEFORE'")
    private Role role;

    @Column(name="member_score",nullable = false)
    @ColumnDefault("0")
    private Long member_score;

    @Column(name="max_score",nullable = false)
    @ColumnDefault("100")
    private Long max_score;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'difficulty1'")
    private Difficulty difficulty;

    public void updateNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public void updateCenter(Center center) {
        this.center = center;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateMemberScore(Long memberScore) {
        this.member_score = memberScore;
    }

    public void updateMaxScore(Long maxScore) {
        this.max_score = maxScore;
    }

    public void updateDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

}
