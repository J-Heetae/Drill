package project.drill.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_nickname",referencedColumnName = "member_nickname",nullable = false)
    private Member member;

    @Column(name="post_content",nullable = false)
    private String postContent;

    @Column(name="post_write_time",nullable = false)
    private LocalDateTime postWriteTime;

    @Column(name="post_video",nullable = false)
    private String postVideo;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'center0'")
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id",nullable = false)
    private Course course;

    @Column(name="post_thumbnail",nullable = false)
    private String postThumbnail;


}

