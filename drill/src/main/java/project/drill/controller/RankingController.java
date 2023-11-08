package project.drill.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import project.drill.domain.Difficulty;
import project.drill.dto.FirstRankingDto;
import project.drill.service.RankingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/ranking")
public class RankingController {

	private final RankingService rankingService;

	@GetMapping("/list")
	@ApiOperation(value = "랭킹 조회")
	public ResponseEntity<List<String>> findAllByMate(
		@RequestParam String centerName,
		@RequestParam String courseName) {
		List<String> rankingList = rankingService.findAllRanking(centerName, courseName);
		return new ResponseEntity<>(rankingList, HttpStatus.OK);
	}

	@GetMapping("/first")
	@ApiOperation(value = "초기 관심지점 가장 첫번째 코스 랭킹 제공")
	public ResponseEntity<FirstRankingDto> findFirstRanking(
		@RequestParam String centerName) {
		FirstRankingDto firstRankingDto = rankingService.findFirstRanking(centerName);
		return new ResponseEntity<>(firstRankingDto, HttpStatus.OK);
	}

	@GetMapping("/findDifficulty")
	@ApiOperation(value = "초기 관심지점 가장 첫번째 코스 랭킹 제공")
	public ResponseEntity<List<Difficulty>> findDifficult(
		@RequestParam String centerName) {
		List<Difficulty> difficultyList = rankingService.findDifficulty(centerName);
		return new ResponseEntity<>(difficultyList, HttpStatus.OK);
	}

	@GetMapping("/findCourseName")
	@ApiOperation(value = "초기 관심지점 가장 첫번째 코스 랭킹 제공")
	public ResponseEntity<List<String>> findCourseName(
		@RequestParam String centerName,
		@RequestParam String difficulty) {
		List<String> courseNameList = rankingService.findCourseName(centerName, difficulty);
		return new ResponseEntity<>(courseNameList, HttpStatus.OK);
	}
}