package project.drill.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Center {
	center0("전체"),
	center1("1호점"),
	center2("2호점"),
	center3("3호점"),
	center4("4호점"),
	center5("5호점"),
	center6("6호점"),
	center7("7호점"),
	center8("8호점"),
	center9("9호점"),
	center10("10호점");
	private String description;
}
