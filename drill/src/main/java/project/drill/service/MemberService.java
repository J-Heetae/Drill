package project.drill.service;

import project.drill.domain.Member;
import project.drill.domain.Post;
import project.drill.dto.PostDto2;

public interface MemberService {
	Member save(PostDto2 postDto2);
}
