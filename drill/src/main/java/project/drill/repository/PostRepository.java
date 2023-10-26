package project.drill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;
import project.drill.domain.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    Optional<Post> findById(Long postId);
    // 닉네임에 해당하는 것 조회
    Page<Post> findAllByMemberMemberNickname(Pageable pageable,String memberNickname);
    // 최신순 조회
    Page<Post> findAllByOrderByPostWriteTimeDesc(Pageable pageable);


   Page<Post> findAllByCenterOrderByPostWriteTimeDesc(Pageable pageable, Center center);

    Page<Post> findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty);

    Page<Post> findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName);
}
