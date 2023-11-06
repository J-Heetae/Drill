package project.drill.service;

import project.drill.domain.Comment;
import project.drill.dto.CommentDto;

import java.util.List;


public interface CommentService {
     void save(CommentDto commentDto);
     void delete(Long commentId);
     List<CommentDto> getCommentList (Long postId);
}
