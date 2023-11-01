package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.drill.domain.Post;
import project.drill.dto.*;

import project.drill.service.MemberService;
import project.drill.service.PostService;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public void sayHi(){
        System.out.println("hi");
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> writePost(@RequestBody PostDto postDto){

        postService.save(postDto);//영상 등록
        memberService.save(postDto); // 유저 레벨 업

        //맴버와 lv보내서 새로운 사람으로 등록
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시글 상세 보기
    @GetMapping("/{postId}")
    public ResponseEntity<?> readPost(@PathVariable Long postId){
        ReadPostDto post = postService.read(postId);
        return new ResponseEntity<>(post,HttpStatus.OK);
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
        Page<Post> myPostPage = postService.findAllByOrder(entirePostPageDto);
        return new ResponseEntity<>(myPostPage,HttpStatus.OK);
    }
}
