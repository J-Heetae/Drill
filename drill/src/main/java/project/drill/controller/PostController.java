package project.drill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;
import project.drill.dto.PostPageAndCourseListDto;
import project.drill.dto.ReadDto;
import project.drill.dto.ReadPostDto;
import project.drill.service.MemberService;
import project.drill.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
	private final PostService postService;
	private final MemberService memberService;

	// 게시글 등록
	@PostMapping
	public ResponseEntity<?> writePost(@RequestBody PostDto postDto) {

		postService.save(postDto);//영상 등록
		memberService.save(postDto); // 유저 레벨 업

		//맴버와 lv보내서 새로운 사람으로 등록
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 게시글 상세 보기
	@PostMapping("/read")
	public ResponseEntity<?> readPost(@RequestBody ReadDto readDto) {
		ReadPostDto post = postService.read(readDto.getMemberNickname(), readDto.getPostId());
		return new ResponseEntity<>(post, HttpStatus.OK);
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}")
	public ResponseEntity<?> deletePost(@RequestParam Long postId) {
		postService.delete(postId);
		return new ResponseEntity<String>("삭제 완료", HttpStatus.OK);
	}

	@PostMapping("/list")
	public ResponseEntity<?> getPostPage(@RequestBody EntirePostPageDto entirePostPageDto) {
		String order = entirePostPageDto.getOrder();
		int page = entirePostPageDto.getPage();
		int size = entirePostPageDto.getSize();

		PostPageAndCourseListDto myPostPage = postService.findAllByOrder(entirePostPageDto);
		if (!myPostPage.getPostPage().isEmpty()) {
			return new ResponseEntity<>(myPostPage, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("게시글이 없습니다.", HttpStatus.OK);
		}
	}
}