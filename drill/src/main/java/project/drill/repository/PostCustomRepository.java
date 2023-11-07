package project.drill.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Center;
import project.drill.domain.Difficulty;
import project.drill.domain.Post;
import project.drill.dto.PostPageDto;


public interface PostCustomRepository {
    List<String> findByCenterNameAndCourseName(String centerName, String courseName);
    Page<PostPageDto> findByLiked(Pageable pageable);
    Page<PostPageDto> findByCenterNameOrderByLiked(Pageable pageable,String centerName);
    Page<PostPageDto> findAllByCenterCenterNameDifficultyOrderByLiked(Pageable pageable,String centerName,String difficulty);
    Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(Pageable pageable, String centerName, String courseName);

    Page<PostPageDto> findByMemberNicknameAndLiked(Pageable pageable,String memberNickname);
    Page<PostPageDto> findByCenterNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName ,String memberNickname);
    Page<PostPageDto> findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String difficulty ,String memberNickname);
    Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(Pageable pageable, String centerName, String courseName ,String memberNickname);
  // ----------------------------------------------------------

    Page<PostPageDto> findAllByOrderByPostWriteTimeDesc(Pageable pageable);


    Page<PostPageDto> findAllByCenterOrderByPostWriteTimeDesc(Pageable pageable, Center center);

    Page<PostPageDto> findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty);

    Page<PostPageDto> findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName);

    Page<PostPageDto> findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable,String memberNickname);


    Page<PostPageDto> findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center,String memberNickname);

    Page<PostPageDto> findAllByCenterAndCourseDifficultyAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty, String memberNickname);

    Page<PostPageDto> findAllByCenterAndCourseCourseNameAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName, String memberNickname);
}
