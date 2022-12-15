package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.club.Club;
import hongik.graduation.hypechick.handler.ApiException;
import hongik.graduation.hypechick.handler.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                    throw new ApiException(ErrorType.EMAIL_ALREADY_EXIST);
                });
    }

    /**
     * 회원 수정
     */
    public Long update(Long id, String username) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        member.updateUsername(username);
        return id;
    }

    /**
     * 클럽 삭제
     */
    public Long deleteClub(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        if (member.getClub()==null){
            throw new ApiException(ErrorType.NOT_HAVE_GROUP);
        }
        member.setClub(null);
        return id;
    }

    /**
     * 목표 설정
     */
    public void setGoal(Long id, String goal){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        member.setGoal(goal);
    }

    /**
     * 회원 삭제
     */
    public Long delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
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
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 타이머 저장
     */
    public Member saveTimer(Long id, Long time) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        member.setTime(time);
        return member;
    }

    private boolean canUpgradeLevel(Member member){
        Level currentLevel = member.getLevel();
        switch (currentLevel){
            case BASIC:
                return (member.getLevelUpTime() >= 7200); // 2시간
            case SILVER:
                return (member.getLevelUpTime() >= 14400); // 4시간
            case GOLD:
                return false;
            default:
                throw new ApiException(ErrorType.LEVEL_NOT_EXIST);
        }
    }

    public void upgradeLevel(Member member) {
        if (canUpgradeLevel(member)) {
            member.upgradeLevel();
        }
    }
}
