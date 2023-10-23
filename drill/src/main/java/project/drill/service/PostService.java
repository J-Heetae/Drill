package project.drill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;
import project.drill.dto.PostDto;

public interface PostService {
    Post save(PostDto postDto);
    Post read(Long postId);

    void delete(Long postId);

    Page<Post> findAllByMemberEmail(Pageable pageable, String memberEmail);
}
