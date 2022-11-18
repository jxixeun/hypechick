package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.club.Club;
import hongik.graduation.hypechick.handler.ApiException;
import hongik.graduation.hypechick.handler.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member).getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 이메일입니다.");
                });
    }

    /**
     * 회원 수정
     */
    public Long update(Long id, String username) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        member.update(username);
        return id;
    }

    /**
     * 클럽가입
     */
    public Long joinClub(Long id, Club club) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        member.setClub(club);
        return id;
    }

    /**
     * 클럽 삭제
     */
    public Long deleteClub(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        member.setClub(null);
        return id;
    }

    /**
     * 클럽 탈퇴
     */
    public Long outClub(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        if (member.getClub() == null){
            throw new ApiException(ErrorType.MOT_HAVE_GROUP);
        }
        Club club = member.getClub();
        Long clubId = member.getClub().getId();
        club.getMembers().remove(member);
        member.setClub(null);
        return clubId;
    }

    /**
     * 목표 설정
     */
    public void setGoal(Long id, String goal){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        member.setGoal(goal);
    }

    /**
     * 회원 삭제
     */
    public Long delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        memberRepository.delete(member);
        return id;
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 개인 회원 조회
     */
    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 타이머 저장
     */
    public Member saveTimer(Long id, Long time) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        member.setTime(time);
        return member;
    }

    private boolean canUpgradeLevel(Member member){
        Level currentLevel = member.getLevel();
        switch (currentLevel){
            case BASIC:
                return (member.getLevelUpTime() >= 3600000); // 1시간
            case SILVER:
                return (member.getLevelUpTime() >= 7200000); // 2시간
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    public void upgradeLevel(Member member) {
        if (canUpgradeLevel(member)) {
            member.upgradeLevel();
        }
    }
}
