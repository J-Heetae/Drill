package project.drill.service;

import java.util.List;

import project.drill.domain.Difficulty;
import project.drill.dto.FirstRankingDto;

public interface RankingService {

	List<String> findAllRanking(String centerName, String courseName);

	List<Difficulty> findDifficulty(String centerName);

	List<String> findCourseName(String centerName, String difficulty);

	FirstRankingDto findFirstRanking(String centerName);

	Long findMyRanking(String memberNickname, String courseName);
}
