package project.drill.repository

import org.springframework.data.jpa.repository.JpaRepository
import project.drill.domain.Member

interface MemberRepository: JpaRepository<Member, String> {
}