package project.drill.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;
import project.drill.domain.Member;
import project.drill.domain.Role;
import project.drill.dto.MemberDto;
import project.drill.dto.PostDto;
import project.drill.repository.CourseRepository;
import project.drill.repository.MemberRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final CourseRepository courseRepository;

	@Transactional
	@Override
	public Member save(PostDto postDto) {
		Optional<Member> member = memberRepository.findByMemberNickname(postDto.getMemberNickname());
		Optional<Course> course = courseRepository.findByCourseNameAndCenterAndIsNewIsTrue(postDto.getCourseName(),
			Center.valueOf(postDto.getCenter()));
		String courseD = course.get().getDifficulty().toString();
		int courseR = courseD.charAt(courseD.length() - 1)-'0';

		String memberD = member.get().getDifficulty().toString();
		int memberR = memberD.charAt(memberD.length() - 1)-'0';

		long score = 0;
		long memberScore = member.get().getMember_score();
		long memberMaxScore = member.get().getMax_score();
		String memberDifficulty = member.get().getDifficulty().toString();
		Difficulty ndifficulty = null;

		if(courseR-memberR<-2){
			score = 0;
		}else if(courseR-memberR==-2){
			score = 1;
		}else if(courseR-memberR==-1){
			score = 5;
		}else if(courseR-memberR==0){
			score = 10;
		}else if(courseR-memberR==1){
			score = 30;
		}else if(courseR-memberR==2){
			score = 50;
		}else {
			score = 100;
		}

		if (memberScore+score>=memberMaxScore){
			score = memberScore+score - memberMaxScore;
			memberMaxScore = Math.round(memberMaxScore*1.5);
			memberR++;
			ndifficulty = Difficulty.valueOf("difficulty"+memberR);

		}else{
			score = memberScore+score;
			ndifficulty = Difficulty.valueOf(memberDifficulty);
		}

		Member member2 = Member.builder()
				.memberId(member.get().getMemberId())
			.memberEmail(member.get().getMemberEmail())
			.center(Center.valueOf(postDto.getCenter()))
			.memberNickname(member.get().getMemberNickname())
			.role(member.get().getRole())
			.member_score(score)
			.max_score(memberMaxScore)
			.difficulty(ndifficulty)
			.build();
		return memberRepository.save(member2);
	}

	@Override
	public MemberDto findMyPage(String memberNickname) {
		Optional<Member> memberOptional = memberRepository.findByMemberNickname(memberNickname);

		MemberDto member = MemberDto.builder()
			.memberNickname(memberOptional.get().getMemberNickname())
			.member_score(memberOptional.get().getMember_score())
			.max_score(memberOptional.get().getMax_score())
			.difficulty(memberOptional.get().getDifficulty().toString())
			.center(memberOptional.get().getCenter().toString())
			.build();
		return member;
	}

	@Override
	public void updateUser(String memberNickname,String center,String memberEmail) {
		Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);

		Member member2 = Member.builder()
			.memberEmail(member.get().getMemberEmail())
			.center(Center.valueOf(center))
			.memberNickname(memberNickname)
			.role(Role.ROLE_DONE)
			.member_score(member.get().getMember_score())
			.max_score(member.get().getMax_score())
			.difficulty(member.get().getDifficulty())
			.build();


		memberRepository.save(member2);
	}

	@Override
	public boolean checkNickname(String nickname) {
		Optional<Member> member = memberRepository.findByMemberNickname(nickname);
		if (!member.isPresent()) {
			return false;
		}
		return true;

	}
}
