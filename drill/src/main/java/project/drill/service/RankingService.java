package project.drill.service;

import java.util.List;



public interface RankingService {

	List<String> findAllRanking(String centerName, String courseName);
}
