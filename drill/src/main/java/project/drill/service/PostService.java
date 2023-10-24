package project.drill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;
import project.drill.dto.PostDto;

import java.util.List;

public interface PostService {
    Post save(PostDto postDto);
    Post read(Long postId);

    void delete(Long postId);

    Page<Post> findAllByMemberEmail(String memberEmail,int page, int size);

    Page<Post> findAllByOrder(String order,int page, int size);
}
