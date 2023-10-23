package project.drill.service;

import project.drill.domain.Post;
import project.drill.dto.PostDto;

public interface PostService {
    Post save(PostDto postDto);
}
