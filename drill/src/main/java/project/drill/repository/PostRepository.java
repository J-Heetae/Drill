package project.drill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Course;
import project.drill.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    Optional<Post> findById(Long postId);

    // 닉네임에 해당하는 것 조회
    Page<Post> findAllByMemberMemberNickname(Pageable pageable,String memberNickname);
    // 최신순 조회
    Page<Post> findAllByOrderByPostWriteTimeDesc(Pageable pageable);
    // 과거순 조회
    Page<Post> findAllByOrderByPostWriteTimeAsc(Pageable pageable);

}
