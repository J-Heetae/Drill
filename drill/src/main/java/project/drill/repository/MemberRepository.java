package project.drill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.drill.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberNickname(String memberNickname);
}
