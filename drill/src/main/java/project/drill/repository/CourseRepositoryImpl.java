package project.drill.repository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.drill.domain.Center;
import project.drill.domain.Difficulty;
import project.drill.domain.QCourse;

@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseCustomRepository{

	private final JPAQueryFactory queryFactory;
	@Override
	public List<Difficulty> findDifficultyByCenterAndIsNewIsTrue(Center center) {
		QCourse qCourse= QCourse.course;
		return queryFactory
			.select(qCourse.difficulty)
			.from(qCourse)
			.where(qCourse.center.eq(center)
				.and(qCourse.isNew.eq(true)))
			.fetch();
	}

	@Override
	public List<String> findCourseNameByIsNewIsTrue(){
		QCourse course = QCourse.course;
		return queryFactory
				.select(course.courseName)
				.from(course)
				.where(course.isNew.eq(true))
				.fetch();
	}

	@Override
	public List<String> findCourseNameByDifficultyAndIsNewIsTrue(Difficulty difficulty) {
		QCourse course = QCourse.course;
		return queryFactory
				.select(course.courseName)
				.from(course)
				.where(course.difficulty.eq(difficulty))
				.fetch();
	}


	@Override
	public List<String> findCourseNameByCenterAndIsNewIsTrue(Center center) {
		QCourse qCourse= QCourse.course;
		return queryFactory
			.select(qCourse.courseName)
			.from(qCourse)
			.where(qCourse.center.eq(center)
				.and(qCourse.isNew.eq(true)))
			.fetch();
	}

	@Override
	public List<String> findCourseNameByCenterAndDifficultyAndIsNewIsTrue(Center center, Difficulty difficulty) {
		QCourse qCourse= QCourse.course;
		return queryFactory
			.select(qCourse.courseName)
			.from(qCourse)
			.where(qCourse.center.eq(center)
				.and(qCourse.difficulty.eq(difficulty))
				.and(qCourse.isNew.eq(true)))
			.fetch();
	}
}
