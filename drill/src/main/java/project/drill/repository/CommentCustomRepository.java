package project.drill.repository;

import project.drill.dto.CommentDto;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentDto> findAllByPostPostIdOrderByCommentWriteTimeDesc (Long postId);
}
