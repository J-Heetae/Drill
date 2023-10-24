package project.drill.repository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import project.drill.domain.Center;
import project.drill.domain.QCourse;
import project.drill.domain.QPost;



@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

	private final JPAQueryFactory queryFactory;


	@Override
	public List<String> findByCenterNameAndCourseName(String centerName, String courseName) {
		QCourse qCourse= QCourse.course;
		QPost qPost= QPost.post;

		return queryFactory
			.select(qPost.member.memberNickname)
			.from(qPost)
			.join(qPost.course, qCourse)
			.where(qCourse.center.eq(Center.valueOf(centerName))
				.and(qCourse.courseName.eq(courseName))
				.and(qCourse.isNew.eq(true)))
			.orderBy(qPost.postWriteTime.asc())  // postWriteTime 오름차순 정렬
			.limit(10)  // 최상위 10개만 가져옴
			.fetch();
	}
}
