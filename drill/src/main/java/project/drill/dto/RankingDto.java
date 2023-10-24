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
public class RankingDto {
	private String centerName;
	private String courseName;
	private String memberNickname;
	private LocalDateTime postWriteTime;
	private String postVideo;
	private String postThumnail;

}
