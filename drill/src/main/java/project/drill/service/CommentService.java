package project.drill.service;

import java.util.List;

import project.drill.dto.CommentDto;
import project.drill.dto.CommentListDto;

public interface CommentService {
	void save(CommentDto commentDto);

	void delete(Long commentId);

	List<CommentListDto> getCommentList(Long postId);
}