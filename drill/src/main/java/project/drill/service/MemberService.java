package project.drill.service;

import project.drill.domain.Member;
import project.drill.dto.MemberDto;
import project.drill.dto.PostDto2;

public interface MemberService {
	Member save(PostDto2 postDto2);

	MemberDto findMyPage(String memberNickname);

	void updateUser(String memberNickname,String center,String memberEmail);
}
