package project.drill.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.drill.domain.Post;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static project.drill.domain.QLiked.liked;
import static project.drill.domain.QPost.post;
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory queryFactory;

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
