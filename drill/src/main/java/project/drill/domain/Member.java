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
    @Column(name="member_email")
    private String memberEmail;

    @Column(name="member_nickname",unique = true, nullable = true)
    private String memberNickname;

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
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



}
