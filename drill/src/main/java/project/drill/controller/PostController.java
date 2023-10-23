package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.drill.domain.Post;
import project.drill.dto.PostDto;
import project.drill.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> writePost(PostDto postDto){
        postService.save(postDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시글 상세 보기
    @GetMapping("/{postId}")
    public ResponseEntity<?> readPost(Long postId){
        Post post = postService.read(postId);
        return new ResponseEntity<Post>(post,HttpStatus.OK);
    }
    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(Long postId){
        postService.delete(postId);
        return new ResponseEntity<String>("삭제 완료",HttpStatus.OK);
    }

    // 내가 작성한 게시글만 보기

    @PostMapping("/me")
    public ResponseEntity<Page<Post>> getToMessageByNickname(@PageableDefault(page = 0, size = 10, sort = "postWriteTime", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String memberEmail) {
        Page<Post> myPostPage = postService.findAllByMemberEmail(pageable,memberEmail);
        return new ResponseEntity<>(myPostPage,HttpStatus.OK);
    }
}
