package project.drill.repository;

import java.util.List;

import project.drill.dto.CommentListDto;

public interface CommentCustomRepository {
	List<CommentListDto> findAllByPostPostIdOrderByCommentWriteTimeAsc(Long postId);
}
