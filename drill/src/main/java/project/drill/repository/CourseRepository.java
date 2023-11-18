package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;
import project.drill.domain.Member;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {
    Optional<Course> findByCourseNameAndCenterAndIsNewIsTrue(String courseName, Center center);
    Optional<Course> findByCourseName(String courseName);
//    List<Course> findAllByIsNewIsTrue();
//    List<Course> findAllByCenterAndIsNewIsTrue(Center center);
//
//    List<Course> findAllByDifficultyAndCenterAndIsNewIsTrue(Difficulty difficulty, Center center);
}
