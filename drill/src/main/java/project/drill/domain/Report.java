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
public class Report {
    @Id
    @GeneratedValue
    @Column(name="report_id")
    private Long reportId;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name="is_check")
    @ColumnDefault("true")
    private boolean isCheck;
}
