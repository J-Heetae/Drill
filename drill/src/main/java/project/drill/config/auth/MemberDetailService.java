package project.drill.config.auth;

import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.drill.domain.Member;
import project.drill.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    System.out.println("MemberDetailService : 진입");
    Member member = loadUserByEmail(email);
    if (member == null) {
      throw new UsernameNotFoundException("User not found");
    }

    // session.setAttribute("loginUser", user);
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
//    try {
//      Optional<Member> memberOptional = memberRepository.findByMemberEmail(email);
//      if (memberOptional.isPresent()) {
//        member = memberOptional.get();
//      }
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
    return member;
  }
}
