package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Comment;

public interface CommentRepository  extends JpaRepository<Comment, Long>{
}
