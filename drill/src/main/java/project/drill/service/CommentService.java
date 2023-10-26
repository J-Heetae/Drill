package project.drill.service;

import project.drill.dto.CommentDto;


public interface CommentService {
     void save(CommentDto commentDto);
     void delete(Long commentId);
}
