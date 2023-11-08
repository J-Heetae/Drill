package project.drill.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.drill.domain.Member;
import project.drill.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = loadUserByEmail(email);
		if (member == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new MemberDetail(member);
	}

	public Member loadUserByEmail(String email) throws UsernameNotFoundException {
		Member member = null;
		Optional<Member> memberOptional = memberRepository.findByMemberEmail(email);
		if (memberOptional.isPresent()) {
			member = memberOptional.get();
		} else {
			throw new RuntimeException();
		}
		return member;
	}
}