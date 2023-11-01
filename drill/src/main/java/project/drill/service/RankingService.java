package project.drill.service;

import java.util.List;

import project.drill.dto.FirstRankingDto;

public interface RankingService {

	List<String> findAllRanking(String centerName, String courseName);

	List<String> findDifficulty(String centerName);

	List<String> findCourseName(String centerName, String difficulty);

	FirstRankingDto findFirstRanking(String centerName);
}
