package project.drill.controller;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project.drill.domain.Difficulty;
import project.drill.dto.FirstRankingDto;
import project.drill.dto.MyRankingDto;
import project.drill.service.RankingService;


import java.util.List;
import java.util.stream.Collectors;

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
			// .stream()
			// .map(Mate::convertToDto)
			// .collect(Collectors.toList());
		System.out.println(rankingList);
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
		List<String> courseNameList = rankingService.findCourseName(centerName,difficulty);
		return new ResponseEntity<>(courseNameList, HttpStatus.OK);
	}

	@PostMapping("/myranking")
	public ResponseEntity<?> myRanking (@RequestBody MyRankingDto myRankingDto){
		Long myRanking = rankingService.findMyRanking(myRankingDto.getMemberNickname(),myRankingDto.getCourseName());
		if(myRanking >0L){
			return new ResponseEntity<>(myRanking,HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
		}
	}

	//
}
