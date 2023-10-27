package project.drill.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;




public interface PostCustomRepository {
	List<String> findByCenterNameAndCourseName(String centerName, String courseName);
    Page<Post> findByLiked(Pageable pageable);
    Page<Post> findByCenterNameOrderByLiked(Pageable pageable,String centerName);
    Page<Post> findAllByCenterCenterNameDifficultyOrderByLiked(Pageable pageable,String centerName,String difficulty);
    Page<Post> findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(Pageable pageable, String centerName, String courseName);

    Page<Post> findByMemberNicknameAndLiked(Pageable pageable,String memberNickname);
    Page<Post> findByCenterNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName ,String memberNickname);
    Page<Post> findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String difficulty ,String memberNickname);
    Page<Post> findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(Pageable pageable, String centerName, String courseName ,String memberNickname);
}
