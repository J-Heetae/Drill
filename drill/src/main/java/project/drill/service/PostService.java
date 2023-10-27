package project.drill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
<<<<<<< Updated upstream
import project.drill.dto.PostDto2;
=======
import project.drill.dto.PostDto;
import project.drill.dto.PostPageAndCourseListDto;
>>>>>>> Stashed changes
import project.drill.dto.ReadPostDto;


import java.util.List;

public interface PostService {
    Post save(PostDto2 postDto2);
    Post read(Long postId);
    void delete(Long postId);
    Page<Post> findAllByMemberEmail(String memberEmail,int page, int size);
    PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto);
}
