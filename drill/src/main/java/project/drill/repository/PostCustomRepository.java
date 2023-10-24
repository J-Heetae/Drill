package project.drill.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;




public interface PostCustomRepository {
	List<String> findByCenterNameAndCourseName(String centerName, String courseName);
    Page<Post> findByLiked(Pageable pageable);
}
