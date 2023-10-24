package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;

import project.drill.dto.PostPageDto;
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
    public ResponseEntity<?> readPost(@PathVariable Long postId){
        Post post = postService.read(postId);
        return new ResponseEntity<Post>(post,HttpStatus.OK);
    }
    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@RequestParam Long postId){
        postService.delete(postId);
        return new ResponseEntity<String>("삭제 완료",HttpStatus.OK);
    }

    // 해당 유저의 게시글만 보기
    @PostMapping("/list")
    public ResponseEntity<?> getMyPostPageByNickname (@RequestBody PostPageDto postPageDto) {
        String memberEmail= postPageDto.getMemberEmail();
        int page = postPageDto.getPage();
        int size = postPageDto.getSize();
        Page<Post> myPostPage = postService.findAllByMemberEmail(memberEmail,page,size);
        return new ResponseEntity<>(myPostPage,HttpStatus.OK);
    }

    @PostMapping("/entire")
    public ResponseEntity<?> getPostPage (@RequestBody EntirePostPageDto entirePostPageDto) {
        String order= entirePostPageDto.getOrder();
        int page = entirePostPageDto.getPage();
        int size = entirePostPageDto.getSize();
        Page<Post> myPostPage = postService.findAllByOrder(order,page,size);
        return new ResponseEntity<>(myPostPage,HttpStatus.OK);
    }
}
