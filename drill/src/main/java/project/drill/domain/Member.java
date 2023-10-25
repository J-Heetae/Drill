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

    @Enumerated(value = EnumType.STRING)
    private Center center;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ROLE_BEFORE'")
    private Role role;

    @Column(name="member_score",nullable = false)
    private Long member_score;

    @Enumerated(value=EnumType.STRING)
    private Difficulty difficulty;



}
