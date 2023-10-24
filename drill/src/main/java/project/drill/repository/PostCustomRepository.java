package project.drill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;

public interface PostCustomRepository {
    Page<Post> findByLiked(Pageable pageable);
}
