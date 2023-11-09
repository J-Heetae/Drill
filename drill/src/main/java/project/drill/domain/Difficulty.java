package project.drill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Difficulty {
    difficulty0("전체"),
    difficulty1("1단계"),
    difficulty2("2단계"),
    difficulty3("3단계"),
    difficulty4("4단계"),
    difficulty5("5단계"),
    difficulty6("6단계"),
    difficulty7("7단계"),
    difficulty8("8단계"),
    difficulty9("9단계"),
    difficulty10("10단계");
    private String description;


}
