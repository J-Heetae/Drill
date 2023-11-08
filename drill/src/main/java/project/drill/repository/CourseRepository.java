package project.drill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.drill.domain.Center;
import project.drill.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {
	Optional<Course> findByCourseNameAndCenterAndIsNewIsTrue(String courseName, Center center);
}