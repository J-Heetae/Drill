package project.drill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.drill.domain.Liked;

public interface LikedRepository extends JpaRepository<Liked, Long> {
	Optional<Liked> findByPostPostIdAndMemberMemberNickname(Long postId, String memberNickname);

	Long countByPostPostId(Long postId);
}