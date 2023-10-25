package project.drill.controller;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.drill.service.RankingService;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
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

		return new ResponseEntity<>(rankingList, HttpStatus.OK);
	}

	//
}
