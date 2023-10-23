package project.drill.domain;

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
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(name="member_email",nullable = false)
    private String memberEmail;

    @Column(name="member_nickname")
    private String memberNickname;

    @Column(name="member_profile")
    private String memberProfile;

    @OneToOne
    @JoinColumn(name="center_id")
    private Center center;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ROLE_BEFORE'")
    private Role role;

    @Column(name="member_score",nullable = false)
    private Long member_score;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'test'")
    private Rank rank;



}
