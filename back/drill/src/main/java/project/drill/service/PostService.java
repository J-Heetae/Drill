package project.drill.service;

import org.springframework.data.domain.Page;
import project.drill.domain.Post;
import project.drill.dto.*;

import java.util.List;

public interface PostService {
    Boolean save(PostDto postDto);
    ReadPostDto read(String memberNickname , Long postId);
    void delete(Long postId);
    PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto);

    List<String> findCourseName(UploadPostDto uploadPostDto);
}
