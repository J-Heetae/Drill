package project.drill.service;

import project.drill.domain.Member;
import project.drill.dto.MemberDto;
import project.drill.dto.PostDto;

public interface MemberService {
	Member save(PostDto postDto);

	MemberDto findMyPage(String memberNickname);

	void updateUser(String memberNickname,String center,String memberEmail);
}
