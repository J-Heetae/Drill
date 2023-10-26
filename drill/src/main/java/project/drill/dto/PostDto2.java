package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto2 {
    private String memberNickname;
    private String center;
    private String postContent;
    private String postVideo;
    private String courseName;
    private String postThumbnail;
}
