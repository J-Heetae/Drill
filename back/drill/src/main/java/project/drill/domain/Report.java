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
public class Report {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_email",referencedColumnName = "member_email",nullable = false)
    private Member member;

    @Column(name="is_check",nullable = false)
    @ColumnDefault("true")
    private boolean isCheck;
}

