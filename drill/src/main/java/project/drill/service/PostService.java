package project.drill.service;

import org.springframework.data.domain.Page;
import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;
import project.drill.dto.PostPageAndCourseListDto;
import project.drill.dto.ReadPostDto;

public interface PostService {
    Post save(PostDto postDto);
    ReadPostDto read(String memberEmail , Long postId);
    void delete(Long postId);
    Page<Post> findAllByMemberEmail(String memberEmail,int page, int size);
    PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto);
}
