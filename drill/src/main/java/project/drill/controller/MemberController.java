package project.drill.controller;

import static project.drill.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static project.drill.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

import java.util.HashMap;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import project.drill.domain.Member;
import project.drill.dto.LoginRequestDto;
import project.drill.dto.MemberDto;
import project.drill.filter.JwtUtil;
import project.drill.service.MemberService;
import project.drill.config.redis.RefreshTokenService;
import project.drill.repository.MemberRepository;
import project.drill.filter.JwtTokenProvider;
import project.drill.service.SocialLoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {

	private final MemberService memberService;

	private final MemberRepository memberRepository;

	private final RefreshTokenService refreshTokenService;

	private final JwtTokenProvider jwtTokenProvider;

	private final JwtUtil jwtUtil;

	private final SocialLoginService socialLoginService;

	@PostMapping("/login")
	public ResponseEntity<String> doSocialLogin(
			@RequestBody @Valid LoginRequestDto request, HttpServletResponse response)
			throws Exception {
		System.out.println(request.getAccessToken());
		Member member = memberRepository.findById(socialLoginService.doSocialLogin(request)).orElseThrow();
		System.out.println("controller member : " + member.toString());
		Map<String, Object> customClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId",
				String.valueOf(member.getMemberId()));

		String accessToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
		String refreshToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				REFRESH_TOKEN_EXPIRATION_TIME, customClaims);

		jwtTokenProvider.setHeaderAccessToken(response, accessToken);

		// 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
		jwtTokenProvider.addHeaderRefreshToken(response, refreshToken);

		refreshTokenService.saveRefreshToken(String.valueOf(member.getMemberId()), refreshToken,
				REFRESH_TOKEN_EXPIRATION_TIME);
		
		// 닉네임 설정 안했으면 로그인 창으로 리다이렉트 시키는 201 응답 전송
		if(member.getMemberNickname() == null) {
			return new ResponseEntity<>("닉네임 설정 필요", HttpStatus.CREATED);
		}
		// 닉네임 설정 했으면 정상 로그인, body에 닉네임 넣어서 주기
		return new ResponseEntity<>(member.getMemberNickname(), HttpStatus.OK);
	}

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
			@RequestHeader HttpHeaders header){
		String kakaoId = jwtTokenProvider.getIdFromToken(header.getFirst("Authorization"));
		System.out.println("kakaoid : " + kakaoId);
		memberService.updateUser(memberNickname,center,kakaoId);
		return new ResponseEntity<>( "Settings updated successfully",HttpStatus.OK);
	}
//	@PutMapping("/settings")
//	@ApiOperation(value = "닉네임, 관심지점 세팅")
//	public ResponseEntity<String> findAllByMate(
//		@RequestParam String memberNickname,
//		@RequestParam String center,
//		@RequestParam String memberEmail){
//		String kakaoId = jwtTokenProvider.getIdFromToken();
//		memberService.updateUser(memberNickname,center,memberEmail);
//	return new ResponseEntity<>( "Settings updated successfully",HttpStatus.OK);
//	}

}
