package project.drill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import project.drill.domain.Center;
import project.drill.domain.Difficulty;
import project.drill.domain.Member;
import project.drill.dto.FirstRankingDto;
import project.drill.repository.CourseRepository;
import project.drill.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
	private final PostRepository postRepository;
	private final CourseRepository courseRepository;
	@Override
	public List<String> findAllRanking(String centerName, String courseName) {
		return postRepository.findByCenterNameAndCourseName(centerName,courseName);
	}

	@Override
	public List<String> findDifficulty(String centerName) {
		return courseRepository.findDifficultyByCenterAndIsNewIsTrue(Center.valueOf(centerName));
	}

	@Override
	public List<String> findCourseName(String centerName, String difficulty) {
		return courseRepository.findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center.valueOf(centerName),
			Difficulty.valueOf(difficulty));
	}

	@Override
	public FirstRankingDto findFirstRanking(String centerName) {

		List<String> difficulties = courseRepository.findDifficultyByCenterAndIsNewIsTrue(Center.valueOf(centerName));
		List<String> courseNames = courseRepository.findCourseNameByCenterAndIsNewIsTrue(Center.valueOf(centerName));
		List<String> rankings = postRepository.findByCenterNameAndCourseName(centerName, courseNames.get(0));

		FirstRankingDto firstRankingDto = FirstRankingDto.builder()
			.ranking(rankings)
			.difficulty(difficulties)
			.courseName(courseNames)
			.build();

		return firstRankingDto;
	}
}
