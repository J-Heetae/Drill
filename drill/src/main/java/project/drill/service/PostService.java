package project.drill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;
import project.drill.dto.ReadPostDto;

import java.util.List;

public interface PostService {
    Post save(PostDto postDto);
    ReadPostDto read(Long postId);

    void delete(Long postId);

    Page<Post> findAllByMemberEmail(String memberEmail,int page, int size);

    Page<Post> findAllByOrder(EntirePostPageDto entirePostPageDto);
}
