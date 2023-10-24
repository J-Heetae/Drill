package project.drill.repository;

import java.util.List;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.drill.domain.Center;
import project.drill.domain.QCourse;
import project.drill.domain.QPost;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;
import javax.persistence.EntityManager;
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
        NumberTemplate<Integer> likedCounts = numberTemplate(Integer.class, "function('likedCounts', {0})", liked.member.memberEmail);
        QueryResults<Post> queryResults = queryFactory
                .selectFrom(post)
                .leftJoin(liked).on(liked.post.eq(post))
                .groupBy(post)
                .orderBy(likedCounts.intValue().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


}