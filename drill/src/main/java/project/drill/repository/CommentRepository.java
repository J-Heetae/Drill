package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Comment;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long>,CommentCustomRepository{

    Long countByPostPostId(Long postId);
}
