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

    @ManyToOne
    @JoinColumn(name="member_email")
    private Member member;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
