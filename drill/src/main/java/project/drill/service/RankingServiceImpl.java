package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.drill.repository.PostCustomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
	private final PostCustomRepository rankingRepository;
	@Override
	public List<String> findAllRanking(String centerName, String courseName) {
		return rankingRepository.findByCenterNameAndCourseName(centerName,courseName);
	}
}
