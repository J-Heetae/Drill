package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Course;
import project.drill.domain.Post;
import project.drill.dto.RankingDto;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long postId);


}
