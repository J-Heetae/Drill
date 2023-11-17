package project.drill.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.drill.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.drill.dto.PostPageDto;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static project.drill.domain.QLiked.liked;
import static project.drill.domain.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

	private final JPAQueryFactory queryFactory;


	@Override
	public List<String> findByCenterNameAndCourseName(Center centerName, String courseName) {
		QCourse qCourse= QCourse.course;
		QPost qPost= QPost.post;

		return queryFactory
				.select(qPost.member.memberNickname)
				.from(qPost)
				.join(qPost.course, qCourse)
				.where(qCourse.center.eq(centerName)
						.and(qCourse.courseName.eq(courseName))
						.and(qCourse.isNew.eq(true)))
				.orderBy(qPost.postWriteTime.asc())  // postWriteTime 오름차순 정렬
				.limit(10)  // 최상위 10개만 가져옴
				.fetch();
	}



	public Page<PostPageDto> findByLiked(Pageable pageable){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true))
				.leftJoin(liked).on(liked.post.eq(post))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findByCenterNameOrderByLiked(Pageable pageable,String centerName){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByDifficultyOrderByLiked(Pageable pageable, String difficulty) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.course.difficulty.eq(Difficulty.valueOf(difficulty))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findAllByCenterCenterNameDifficultyOrderByLiked(Pageable pageable,String centerName,String difficulty){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.difficulty.eq(Difficulty.valueOf(difficulty)))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameOrderByLiked(Pageable pageable,String centerName,String courseName){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.courseName.eq(courseName))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findByMemberNicknameAndLiked(Pageable pageable, String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByMemberNicknameAndDifficultyOrderByLiked(Pageable pageable, String memberNickname, String difficulty) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.course.difficulty.eq(Difficulty.valueOf(difficulty))
						.and(post.member.memberNickname.eq(memberNickname))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findByCenterNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.member.memberNickname.eq(memberNickname))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	public Page<PostPageDto> findAllByCenterCenterNameDifficultyAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String difficulty, String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.difficulty.eq(Difficulty.valueOf(difficulty)))
						.and(post.member.memberNickname.eq(memberNickname))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}
	public Page<PostPageDto> findAllByCenterCenterNameAndCourseCourseNameAndMemberNicknameOrderByLiked(Pageable pageable,String centerName,String courseName,String memberNickname){
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.leftJoin(liked).on(liked.post.eq(post))
				.where(post.center.eq(Center.valueOf(centerName))
						.and(post.course.courseName.eq(courseName))
						.and(post.member.memberNickname.eq(memberNickname))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByOrderByPostWriteTimeDesc(Pageable pageable) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterOrderByPostWriteTimeDesc(Pageable pageable, Center center) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.center.eq(center)
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Difficulty difficulty) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.difficulty.eq(difficulty)
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterAndCourseDifficultyOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.center.eq(center)
						.and(post.course.difficulty.eq(difficulty))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterAndCourseCourseNameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.center.eq(center)
						.and(post.course.courseName.eq(courseName))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByMemberMemberNicknameAndDifficultyOrderByPostWriteTimeDesc(Pageable pageable, String memberNickname, Difficulty difficulty) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.course.difficulty.eq(difficulty))
						.and(post.course.isNew.eq(true)))

				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.center.eq(center))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterAndCourseDifficultyAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, Difficulty difficulty, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.center.eq(center))
						.and(post.course.difficulty.eq(difficulty))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findAllByCenterAndCourseCourseNameAndMemberMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, Center center, String courseName, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.center.eq(center))
						.and(post.course.courseName.eq(courseName))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByMemberMemberNicknameAndLiked(Pageable pageable, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true)
						.and(post.member.memberNickname.eq(memberNickname)))
				.leftJoin(liked).on(liked.post.eq(post))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByCourseNameAndMemberNicknameByLiked(Pageable pageable, String courseName, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true)
						.and(post.member.memberNickname.eq(memberNickname))
						.and(post.course.courseName.eq(courseName)))
				.leftJoin(liked).on(liked.post.eq(post))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByCourseNameAndMemberNicknameOrderByPostWriteTimeDesc(Pageable pageable, String courseName, String memberNickname) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.member.memberNickname.eq(memberNickname)
						.and(post.course.courseName.eq(courseName))
						.and(post.course.isNew.eq(true)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByCourseNameByLiked(Pageable pageable, String courseName) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true)
						.and(post.course.courseName.eq(courseName)))
				.leftJoin(liked).on(liked.post.eq(post))
				.groupBy(post)
				.orderBy(liked.likedId.count().coalesce(0L).desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Page<PostPageDto> findByCourseNameOrderByPostWriteTimeDesc(Pageable pageable, String courseName) {
		QPost post = QPost.post;
		QLiked liked = QLiked.liked;
		QueryResults<Tuple> queryResults = queryFactory
				.select(post.postId,post.postThumbnail)
				.from(post)
				.where(post.course.isNew.eq(true)
						.and(post.course.courseName.eq(courseName)))
				.groupBy(post)
				.orderBy(post.postWriteTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<PostPageDto> postPageDtoList = queryResults.getResults().stream()
				.map(tuple -> new PostPageDto(
						tuple.get(post.postId),
						tuple.get(post.postThumbnail)
				))
				.collect(Collectors.toList());
		return new PageImpl<>(postPageDtoList,pageable, queryResults.getTotal());
	}

	@Override
	public Long findMyRanking(String memberNickname, Long courseId) {
		QPost post = QPost.post;
		List<Long> rankings = queryFactory
				.select(post.postId)
				.from(post)
				.where(post.course.courseId.eq(courseId)
						.and(post.course.isNew.eq(true))
						.and(post.postWriteTime.lt(
								JPAExpressions.select(post.postWriteTime.min())
										.from(post)
										.where(post.member.memberNickname.eq(memberNickname)
												.and(post.course.courseId.eq(courseId))
												.and(post.course.isNew.eq(true)))
						)))
				.fetch();
		return (long) rankings.size() + 1;
	}

}