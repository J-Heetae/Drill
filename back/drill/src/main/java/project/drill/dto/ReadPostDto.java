package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadPostDto {
    private String memberNickname;
    private String centerName;
    private String postContent;
    private String postVideo;
    private LocalDateTime postWriteTime;
    private String courseName;
    private Long likedCount;
    private Long commentCount;
    private boolean isLiked;
}
