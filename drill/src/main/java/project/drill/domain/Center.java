package project.drill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Center {
	center1("center1"),
	center2("center2"),
	center3("center3");
	private String description;
}
