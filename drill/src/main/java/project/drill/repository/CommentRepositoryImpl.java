package project.drill.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.drill.domain.QComment;
import project.drill.dto.CommentListDto;

@RequiredArgsConstructor
public class CommentRepositoryImpl {
	private final JPAQueryFactory queryFactory;

	public List<CommentListDto> findAllByPostPostIdOrderByCommentWriteTimeAsc(Long postId) {
		QComment qComment = QComment.comment;
		List<Tuple> lists = queryFactory
			.select(qComment.member.member_score, qComment.member.memberNickname, qComment.commentContent)
			.from(qComment)
			.where(qComment.post.postId.eq(postId))
			.orderBy(qComment.commentWriteTime.asc())
			.fetch();
		return lists.stream()
			.map(tuple -> CommentListDto.builder()
				.member_score(tuple.get(qComment.member.member_score))
				.memberNickname(tuple.get(qComment.member.memberNickname))
				.commentContent(tuple.get(qComment.commentContent))
				.build())
			.collect(Collectors.toList());
	}
}