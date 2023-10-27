package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Member;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseNameAndCenterAndIsNewIsTrue(String courseName, Center center);
}
