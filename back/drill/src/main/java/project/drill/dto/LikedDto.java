package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.drill.domain.Member;
import project.drill.domain.Post;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikedDto {
    private String memberNickname;
    private Long postId;
}
