package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.drill.domain.Comment;
import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;
import project.drill.service.CommentService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping()
    public ResponseEntity<?> saveComment (@RequestBody CommentDto commentDto){
        commentService.save(commentDto);
        return new ResponseEntity<>("작성 완료", HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.delete(commentId);
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<?> getCommentList(@PathVariable Long postId){
        List<CommentListDto> commentList = commentService.getCommentList(postId);
        return new ResponseEntity<>(commentList,HttpStatus.OK);
    }
}
