package project.drill.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Builder
public class Liked {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="liked_id")
    private Long likedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_email",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
    private Post post;
}
