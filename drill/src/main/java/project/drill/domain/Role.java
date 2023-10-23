package project.drill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_BOSS("ROLE_BOSS"),
    ROLE_BEFORE("ROLE_BEFORE"),
    ROLE_DONE("ROLE_DONE");
    private String description;
}

