package project.drill.controller;

import static project.drill.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static project.drill.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

import java.util.HashMap;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import project.drill.config.auth.MemberDetail;
import project.drill.domain.Center;
import project.drill.domain.Member;
import project.drill.dto.LocalLoginDto;
import project.drill.dto.LocalRegisterDto;
import project.drill.dto.LoginRequestDto;
import project.drill.dto.MemberDto;
import project.drill.dto.SettingDto;
import project.drill.filter.JwtUtil;
import project.drill.service.MemberService;
import project.drill.config.redis.RefreshTokenService;
import project.drill.repository.MemberRepository;
import project.drill.filter.JwtTokenProvider;
import project.drill.service.SocialLoginService;

@Slf4j
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

	private final BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<String> doSocialLogin(
			@RequestBody @Valid LoginRequestDto request, HttpServletResponse response)
			throws Exception {
		System.out.println(request.getSocialToken());
		Member member = memberRepository.findById(socialLoginService.doSocialLogin(request))
				.orElseThrow();
		System.out.println("controller member : " + member.toString());
		Map<String, Object> customClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId",
				String.valueOf(member.getMemberId()));

		String accessToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
		System.out.println(accessToken);
		String refreshToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				REFRESH_TOKEN_EXPIRATION_TIME, customClaims);
		System.out.println(refreshToken);
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);

		// 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
		jwtTokenProvider.addHeaderRefreshToken(response, refreshToken);

		refreshTokenService.saveRefreshToken(String.valueOf(member.getMemberId()), refreshToken,
				REFRESH_TOKEN_EXPIRATION_TIME);

		// 닉네임 설정 안했으면 로그인 창으로 리다이렉트 시키는 201 응답 전송
		// getMemberNickname 말고 getRole로 하는게 더 좋은 방법일 수 있음
		if (member.getMemberNickname() == null) {
			return new ResponseEntity<>("닉네임 설정 필요", HttpStatus.CREATED);
		}
		if (member.getCenter() == Center.center0) {
			return new ResponseEntity<>("관심지점 설정 필요", HttpStatus.CREATED);
		}
		String ans = member.getMemberNickname() +" "+member.getCenter().toString();
		// 닉네임 설정 했으면 정상 로그인, body에 닉네임 넣어서 주기
		return new ResponseEntity<>(ans, HttpStatus.OK);
	}

	@GetMapping("/mypage")
	public ResponseEntity<?> readPost(@RequestParam String memberNickname) {
		MemberDto member = memberService.findMyPage(memberNickname);
		return new ResponseEntity<MemberDto>(member, HttpStatus.OK);
	}

	@PutMapping("/settings")
	@ApiOperation(value = "닉네임, 관심지점 세팅")
	public ResponseEntity<String> findAllByMate(
			@RequestBody SettingDto settingDto,
			@RequestHeader HttpHeaders header) {
		String kakaoId = jwtTokenProvider.getIdFromToken(header.getFirst("Authorization"));
		log.info("kakao Id : " + kakaoId);
		log.info("nickname : " + settingDto.getMemberNickname());
		log.info("center : " + settingDto.getCenter());
		memberService.updateUser(settingDto.getMemberNickname(), settingDto.getCenter(), kakaoId);
		return new ResponseEntity<>("Settings updated successfully", HttpStatus.OK);
	}
	@GetMapping("/nickname/{nickname}")
	public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
		System.out.println("come?");
		boolean isDuplicate = memberService.checkNickname(nickname);
		return new ResponseEntity<>(isDuplicate, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@AuthenticationPrincipal MemberDetail memberDetail,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpStatus status;
		String refreshToken = jwtUtil.resolveToken(request);
		Object message = null;
		log.debug("token : {}, memberDetail : {}", refreshToken, memberDetail);
		if (refreshToken.equals(refreshTokenService.getRefreshToken(
				String.valueOf(memberDetail.getMember().getMemberId())))) {
			Map<String, Object> customClaims = new HashMap<>();
			customClaims.put("memberId", String.valueOf(memberDetail.getMember().getMemberId()));
			String newAccessToken = jwtTokenProvider.generateToken(memberDetail.getUsername(),
					ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
			String newRefreshToken = jwtTokenProvider.generateToken(memberDetail.getUsername(),
					REFRESH_TOKEN_EXPIRATION_TIME, customClaims);
			jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
			jwtTokenProvider.addHeaderRefreshToken(response, newRefreshToken);
			log.debug("token : {}", newAccessToken);
			// 200 return
			log.debug("정상적으로 액세스토큰 재발급!!!");
			status = HttpStatus.OK;
			message = memberDetail.getMember().getMemberNickname();
		} else {
			// 401 return
			log.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
			message = "refreshToken FAIL";
		}
		return new ResponseEntity<>(message, status);
	}

	// 소셜 로그인 회원가입
	@PostMapping("/regist")
	public ResponseEntity<String> localRegister(@RequestBody LocalRegisterDto localRegisterDto) throws Exception {
		HttpStatus status;
		String message;
		String password = passwordEncoder.encode(localRegisterDto.getPassword());
		LocalRegisterDto dto = LocalRegisterDto.builder()
				.email(localRegisterDto.getEmail())
				.password(password)
				.build();
		// 중복 : 205 RESET_CONTENT 반환
		// 회원 가입 완료 : 200 OK 반환
		if (memberService.localRegister(dto) == null) {
			message = "중복된 회원입니다.";
			status = HttpStatus.RESET_CONTENT;
		} else {
			message = "회원가입 완료";
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(message, status);
	}

	@PostMapping("/locallogin")
	public ResponseEntity<String> localLogin(@RequestBody LocalLoginDto localLoginDto,
			HttpServletResponse response) throws Exception {

		Member member = memberService.localLogin(localLoginDto);

		if (member == null) {
			return new ResponseEntity<>("존재하지 않거나 비밀번호가 틀렸습니다", HttpStatus.CREATED);
		}

		Map<String, Object> customClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId",
				String.valueOf(member.getMemberId()));

		String accessToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
		System.out.println(accessToken);
		String refreshToken = jwtTokenProvider.generateToken(member.getMemberEmail(),
				REFRESH_TOKEN_EXPIRATION_TIME, customClaims);
		System.out.println(refreshToken);
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);

		// 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
		jwtTokenProvider.addHeaderRefreshToken(response, refreshToken);

		refreshTokenService.saveRefreshToken(String.valueOf(member.getMemberId()), refreshToken,
				REFRESH_TOKEN_EXPIRATION_TIME);

		// 닉네임 설정 안했으면 로그인 창으로 리다이렉트 시키는 201 응답 전송
		if (member.getMemberNickname() == null) {
			return new ResponseEntity<>("닉네임 설정 필요", HttpStatus.CREATED);
		}
		if (member.getCenter() == Center.center0) {
			return new ResponseEntity<>("관심지점 설정 필요", HttpStatus.CREATED);
		}

		String ans = member.getMemberNickname() +" "+member.getCenter().toString();
		// 닉네임 설정 했으면 정상 로그인, body에 닉네임 넣어서 주기
		return new ResponseEntity<>(ans, HttpStatus.OK);

	}
}