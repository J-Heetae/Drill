package project.drill.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.drill.domain.*;
import project.drill.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public List<CommentDto> findAllByPostPostIdOrderByCommentWriteTimeDesc (Long postId){
        QComment qComment= QComment.comment;
        List<Tuple> lists= queryFactory
                .select(qComment.post.postId,qComment.member.memberNickname,qComment.commentContent)
                .from(qComment)
                .where(qComment.post.postId.eq(postId))
                .orderBy(qComment.commentWriteTime.desc())
                .fetch();
        return lists.stream()
                .map(tuple -> CommentDto.builder()
                        .postId(tuple.get(qComment.post.postId))
                        .memberNickname(tuple.get(qComment.member.memberNickname))
                        .commentContent(tuple.get(qComment.commentContent))
                        .build())
                .collect(Collectors.toList());
    }
}

