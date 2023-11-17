package project.drill.repository;

import java.util.List;

import project.drill.domain.Center;
import project.drill.domain.Difficulty;

public interface CourseCustomRepository {
	List<Difficulty> findDifficultyByCenterAndIsNewIsTrue(Center center);

	List<String> findCourseNameByIsNewIsTrue();

	List<String> findCourseNameByDifficultyAndIsNewIsTrue(Difficulty difficulty);
	List<String> findCourseNameByCenterAndIsNewIsTrue(Center center);

	List<String> findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center center, Difficulty difficulty);

}
