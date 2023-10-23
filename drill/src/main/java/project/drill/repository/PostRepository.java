package project.drill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Course;
import project.drill.domain.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long postId);

    Page<Post> findAllByMemberNickname(Pageable pageable, String memberNickname);

}
