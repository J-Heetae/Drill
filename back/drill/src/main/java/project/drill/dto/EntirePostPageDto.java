package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntirePostPageDto {
    private String centerName;
    private String difficulty;
    private String courseName;
    private String memberNickname;
    private String order;
    private int page;
    private int size;

}
