package project.drill.service;

import java.util.Optional;

import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.drill.domain.Center;
import project.drill.domain.Course;
import project.drill.domain.Difficulty;
import project.drill.domain.Member;
import project.drill.domain.Role;
import project.drill.dto.LocalLoginDto;
import project.drill.dto.LocalRegisterDto;
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
	private final BCryptPasswordEncoder passwordEncoder;
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

		if(member.isPresent()) {
			Member updateMember = member.get();
			updateMember.updateCenter(Center.valueOf((postDto.getCenter())));
			updateMember.updateMemberScore(score);
			updateMember.updateMaxScore(memberMaxScore);
			updateMember.updateDifficulty(ndifficulty);
			return memberRepository.save(updateMember);
		} else {
			return null;
		}
	}

	@Override
	public MemberDto findMyPage(String memberNickname) {
		Optional<Member> memberOptional = memberRepository.findByMemberNickname(memberNickname);

		if(memberOptional.isPresent()) {
			MemberDto member = MemberDto.builder()
					.memberNickname(memberOptional.get().getMemberNickname())
					.member_score(memberOptional.get().getMember_score())
					.max_score(memberOptional.get().getMax_score())
					.difficulty(memberOptional.get().getDifficulty().toString())
					.center(memberOptional.get().getCenter().toString())
					.build();
			return member;
		} else {
			return null;
		}
	}

	@Override
	public void updateUser(String memberNickname,String center,String memberEmail) {
		Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);
		if(member.isPresent()) {
			Member updateMember = member.get();
			updateMember.updateNickname(memberNickname);
			updateMember.updateCenter(Center.valueOf(center));
			updateMember.updateRole(Role.ROLE_DONE);
			memberRepository.save(updateMember);
		}
	}

	@Override
	public boolean checkNickname(String nickname) {
		Optional<Member> member = memberRepository.findByMemberNickname(nickname);
		if (!member.isPresent()) {
			return false;
		}
		return true;

	}

	@Override
	public Member localRegister(LocalRegisterDto localRegisterDto) {
		Optional<Member> member = memberRepository.findByMemberEmail(localRegisterDto.getEmail());
		if(!member.isPresent()) {
			Member newMember = Member.builder().
					memberEmail(localRegisterDto.getEmail()).
					password(localRegisterDto.getPassword()).
					build();
			memberRepository.save(newMember);
			return newMember;
		} else {
			// 중복
			return null;
		}
	}
	public Member localLogin(LocalLoginDto localLoginDto) {
		Optional<Member> member = memberRepository.findByMemberEmail(localLoginDto.getEmail());
		if(member.isPresent() && passwordEncoder.matches(localLoginDto.getPassword(), member.get().getPassword())) {
			return member.get();
		} else {
			return null;
		}
	}
}
