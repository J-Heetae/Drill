package project.drill.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Center;
import project.drill.domain.Difficulty;
import project.drill.domain.Post;
import project.drill.dto.PostPageDto;


public interface PostCustomRepository {
    List<String> findByCenterNameAndCourseName(Center centerName, String courseName);
    Page<PostPageDto> findByLiked(Pageable pageable);
    Page<PostPageDto> findByCenterNameOrderByLiked(Pageable pageable,String centerName);
    Page<PostPageDto> findByDifficultyOrderByLiked(Pageable pageable, String difficulty);
    Page<PostPageDto> findAllByCenterCenterNameDifficultyOrderByLiked(Pageable pageable,String centerName,String difficulty);
    Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(Pageable pageable, String centerName, String courseName);

    Page<PostPageDto> findByMemberNicknameAndLiked(Pageable pageable,String memberNickname);

    Page<PostPageDto> findByMemberNicknameAndDifficultyOrderByLiked(Pageable pageable, String memberNickname, String difficulty);
    Page<PostPageDto> findByCenterNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName ,String memberNickname);
    Page<PostPageDto> findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String difficulty ,String memberNickname);
    Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(Pageable pageable, String centerName, String courseName ,String memberNickname);

    Page<PostPageDto> findAllByOrderByPostWriteTimeDesc(Pageable pageable);

    Page<PostPageDto> findAllByCenterOrderByPostWriteTimeDesc(Pageable pageable, Center center);
    Page<PostPageDto> findAllByDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Difficulty difficulty);

    Page<PostPageDto> findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty);

    Page<PostPageDto> findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName);

    Page<PostPageDto> findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable,String memberNickname);

    Page<PostPageDto> findAllByMemberMemberNicknameAndDifficultyOrderByPostWriteTimeDesc(Pageable pageable,String memberNickname, Difficulty difficulty);
    Page<PostPageDto> findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center,String memberNickname);

    Page<PostPageDto> findAllByCenterAndCourseDifficultyAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty, String memberNickname);

    Page<PostPageDto> findAllByCenterAndCourseCourseNameAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName, String memberNickname);
    Page<PostPageDto> findByMemberMemberNicknameAndLiked(Pageable pageable, String memberEmail);
    Page<PostPageDto> findByCourseNameAndMemberNicknameByLiked(Pageable pageable, String courseName, String memberNickname);
    Page<PostPageDto> findByCourseNameAndMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, String courseName, String memberNickname);

    Page<PostPageDto> findByCourseNameByLiked(Pageable pageable, String courseName);

    Page<PostPageDto> findByCourseNameOrderByPostWriteTimeDesc(Pageable pageable, String courseName);

    Long findMyRanking(String memberNickname, Long courseId);


}
