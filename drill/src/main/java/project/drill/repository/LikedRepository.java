package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Liked;
import project.drill.domain.Post;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long>{
    Optional<Liked> findByPostPostIdAndMemberMemberEmail(Long postId, String memberEmail);
    Long countByPostPostId(Long postId);
}
