package project.drill.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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