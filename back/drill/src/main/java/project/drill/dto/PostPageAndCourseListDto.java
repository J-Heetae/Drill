package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import project.drill.domain.Course;
import project.drill.domain.Post;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageAndCourseListDto {
    Page<PostPageDto> postPage;
    List<String> courseNameList;
}
