package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Liked;
import project.drill.domain.Post;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long>{
    Optional<Liked> findByPostPostIdAndMemberMemberNickname(Long postId, String memberNickname);
    Long countByPostPostId(Long postId);
}
