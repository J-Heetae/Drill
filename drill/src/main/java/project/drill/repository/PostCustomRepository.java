package project.drill.repository;

import java.util.List;




public interface PostCustomRepository {

	List<String> findByCenterNameAndCourseName(String centerName, String courseName);
}
