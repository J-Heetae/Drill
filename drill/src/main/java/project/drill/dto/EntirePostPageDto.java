package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.drill.domain.Course;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntirePostPageDto {
    private String centerName;
    private String difficulty;
    private String courseName;
    private String order;
    private String memberNickname;
    private int page;
    private int size;
    private List<Course> courseList;
}
