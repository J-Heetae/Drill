package project.drill.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirstRankingDto {
	List<String> ranking;
	List<Difficulty> difficulty;
	List<String> courseName;
}
