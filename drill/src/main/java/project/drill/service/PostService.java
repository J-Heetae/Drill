package project.drill.service;

import project.drill.domain.Post;
import project.drill.dto.EntirePostPageDto;
import project.drill.dto.PostDto;
import project.drill.dto.PostPageAndCourseListDto;
import project.drill.dto.ReadPostDto;

public interface PostService {
	Post save(PostDto postDto);

	ReadPostDto read(String memberNickname, Long postId);

	void delete(Long postId);

	PostPageAndCourseListDto findAllByOrder(EntirePostPageDto entirePostPageDto);
}