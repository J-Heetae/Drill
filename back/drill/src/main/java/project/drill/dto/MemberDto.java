package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
	private String memberNickname;
	private long member_score;
	private long max_score;
	private String center;
	private  String difficulty;
}
