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
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="course_id")
    private Long courseId;

    @Column(name="course_name",nullable = false)
    private String courseName;

    @Column(name="course_difficulty", nullable = false)
    private String courseDifficulty;

    @Enumerated(value=EnumType.STRING)
    @ColumnDefault("'center'")
    private Center center;

    @ColumnDefault("true")
    @Column(name="is_new",nullable = false)
    private boolean isNew;



}
