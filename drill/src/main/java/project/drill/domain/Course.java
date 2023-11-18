
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

    @Enumerated(value=EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(value=EnumType.STRING)
    private Center center;

    @ColumnDefault("true")
    @Column(name="is_new",nullable = false)
    private boolean isNew;



}