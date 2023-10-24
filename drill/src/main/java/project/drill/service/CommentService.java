package project.drill.service;

import org.springframework.stereotype.Service;
import project.drill.domain.Comment;
import project.drill.dto.CommentDto;


public interface CommentService {
     void save(CommentDto commentDto);

     void delete(Long commentId);
}
