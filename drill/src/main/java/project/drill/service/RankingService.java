package project.drill.service;
import project.drill.dto.RankingDto;
import java.util.List;



public interface RankingService {

	List<String> findAllRanking(String centerName, String courseName);
}
