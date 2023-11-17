package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.drill.dto.LikedDto;
import project.drill.service.LikedService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liked")
public class LikedController {
    private final LikedService likedService;
    @PostMapping
    public ResponseEntity<?> liked (@RequestBody LikedDto likedDto){
        likedService.save(likedDto);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
}
