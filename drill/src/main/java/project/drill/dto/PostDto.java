package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.drill.domain.Center;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private String memberNickname;
    private String centerName;
    private String postContent;
    private String postVideo;
    private Long courseId;
    private String postThumbnail;
}
