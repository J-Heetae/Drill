package project.drill.repository;

import java.util.List;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.drill.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static project.drill.domain.QLiked.liked;
import static project.drill.domain.QPost.post;


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


	public Page<Post> findByLiked(Pageable pageable){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}

	public Page<Post> findByCenterNameOrderByLiked(Pageable pageable,String centerName){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName)))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}
	public Page<Post> findAllByCenterCenterNameDifficultyOrderByLiked(Pageable pageable,String centerName,String difficulty){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.difficulty.eq(Difficulty.valueOf(difficulty))))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}
	public Page<Post> findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(Pageable pageable,String centerName,String courseName){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.courseName.eq(courseName)))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}

	public Page<Post> findByMemberNicknameAndLiked(Pageable pageable, String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.member.memberNickname.eq(memberNickname))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}

	public Page<Post> findByCenterNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.member.memberNickname.eq(memberNickname)))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}
	public Page<Post> findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String difficulty, String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.difficulty.eq(Difficulty.valueOf(difficulty)))
						.and(post.member.memberNickname.eq(memberNickname)))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}
	public Page<Post> findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String courseName,String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Post> queryResults = queryFactory
				.selectFrom(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.courseName.eq(courseName))
						.and(post.member.memberNickname.eq(memberNickname)))
				.groupBy(post)
				.orderBy(liked.likedId.count().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		return new PageImpl<>(queryResults.getResults(),pageable, queryResults.getTotal());
	}

}