package project.drill.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import project.drill.dto.MemberDto;
import project.drill.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/mypage")
	public ResponseEntity<?> readPost(@RequestParam String memberNickname){
		MemberDto member = memberService.findMyPage(memberNickname);
		return new ResponseEntity<MemberDto>(member, HttpStatus.OK);
	}

	@PutMapping("/settings")
	@ApiOperation(value = "닉네임, 관심지점 세팅")
	public ResponseEntity<String> findAllByMate(
		@RequestParam String memberNickname,
		@RequestParam String center,
		@RequestParam String memberEmail){
		memberService.updateUser(memberNickname,center,memberEmail);
	return new ResponseEntity<>( "Settings updated successfully",HttpStatus.OK);
	}

}
