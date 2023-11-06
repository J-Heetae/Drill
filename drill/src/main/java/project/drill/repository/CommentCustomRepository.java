package project.drill.repository;

import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentListDto> findAllByPostPostIdOrderByCommentWriteTimeAsc (Long postId);
}
