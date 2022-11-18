package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.login.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findByClub_Id(Long clubId);
    List<Member> findBySocialType(SocialType socialType);
}
