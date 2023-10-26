package project.drill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.drill.service.LikedService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liked")
public class LikedController {
    private final LikedService likedService;
}
