package project.drill.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;
import project.drill.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
	private final CommentService commentService;

	// 댓글 작성
	@PostMapping()
	public ResponseEntity<?> saveComment(@RequestBody CommentDto commentDto) {
		commentService.save(commentDto);
		return new ResponseEntity<>("작성 완료", HttpStatus.OK);
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
		commentService.delete(commentId);
		return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
	}

	@GetMapping("/list/{postId}")
	public ResponseEntity<?> getCommentList(@PathVariable Long postId) {
		List<CommentListDto> commentList = commentService.getCommentList(postId);
		return new ResponseEntity<>(commentList, HttpStatus.OK);
	}
}
