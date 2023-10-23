package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.drill.dto.PostDto;
import project.drill.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    @PostMapping
    ResponseEntity<?> registerPost(PostDto postDto){
        postService.save(postDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
