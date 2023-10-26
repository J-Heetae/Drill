package project.drill.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;
import project.drill.domain.Member;
import project.drill.dto.PostDto2;
import project.drill.repository.CourseRepository;
import project.drill.repository.MemberRepository;
import project.drill.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final CourseRepository courseRepository;

	@Override
	public Member save(PostDto2 postDto2) {
		Optional<Member> member = memberRepository.findByMemberNickname(postDto2.getMemberNickname());
		Optional<Course> course = courseRepository.findByCourseNameAndCenterAndIsNewIsTrue(postDto2.getCourseName(),
			Center.valueOf(postDto2.getCenter()));
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
			.memberEmail(member.get().getMemberEmail())
			.center(Center.valueOf(postDto2.getCenter()))
			.memberNickname(member.get().getMemberNickname())
			.role(member.get().getRole())
			.member_score(score)
			.max_score(memberMaxScore)
			.difficulty(ndifficulty)
			.build();
		return memberRepository.save(member2);
	}
}
