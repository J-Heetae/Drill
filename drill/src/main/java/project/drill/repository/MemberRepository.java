package project.drill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.drill.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByMemberNickname(String memberNickname);

	Optional<Member> findByMemberEmail(String memberEmail);
}