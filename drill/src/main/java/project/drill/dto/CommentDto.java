package project.drill.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.drill.domain.Member;
import project.drill.domain.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long postId;
    private String memberNickname;
    private String commentContent;



}
