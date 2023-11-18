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

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> writePost(@RequestBody PostDto postDto){
        Boolean isExist = postService.save(postDto);//영상 등록
        memberService.save(postDto); // 유저 레벨 업
        //맴버와 lv보내서 새로운 사람으로 등록
        if(isExist){
        return new ResponseEntity<>(HttpStatus.OK);}
        else{
            return new ResponseEntity<String>("이미 등록한 코스입니다.",HttpStatus.CONFLICT);
        }
    }

    // 게시글 상세 보기
    @PostMapping("/read")
    public ResponseEntity<?> readPost(@RequestBody ReadDto readDto){
        ReadPostDto post = postService.read(readDto.getMemberNickname(),readDto.getPostId());
        return new ResponseEntity<>(post,HttpStatus.OK);
    }
    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@RequestParam Long postId){
        postService.delete(postId);
        return new ResponseEntity<String>("삭제 완료",HttpStatus.OK);
    }




    @PostMapping("/list")
    public ResponseEntity<?> getPostPage (@RequestBody EntirePostPageDto entirePostPageDto) {
        String order= entirePostPageDto.getOrder();
        int page = entirePostPageDto.getPage();
        int size = entirePostPageDto.getSize();
        PostPageAndCourseListDto myPostPage = postService.findAllByOrder(entirePostPageDto);
        if(!myPostPage.getPostPage().isEmpty()){
        return new ResponseEntity<>(myPostPage,HttpStatus.OK);}
        else{
            return new ResponseEntity<String>("게시글이 없습니다.",HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping("/course")
    public ResponseEntity<?> getCourse(@RequestBody UploadPostDto uploadPostDto){
        List<String> courseNameList = postService.findCourseName(uploadPostDto);
        if(!courseNameList.isEmpty()){
            return new ResponseEntity<>(courseNameList,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>("코스가 없습니다.",HttpStatus.NO_CONTENT);
        }
    }
}
