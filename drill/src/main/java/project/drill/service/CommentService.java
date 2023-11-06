package project.drill.service;

import project.drill.domain.Comment;
import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;

import java.util.List;


public interface CommentService {
     void save(CommentDto commentDto);
     void delete(Long commentId);
     List<CommentListDto> getCommentList (Long postId);
}
