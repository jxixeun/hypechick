package hongik.graduation.hypechick.club;

import hongik.graduation.hypechick.handler.ApiException;
import hongik.graduation.hypechick.handler.ErrorType;
import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberService;
import hongik.graduation.hypechick.timer.StudyTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberService memberService;
    private final StudyTimeService studyTimeService;

    /**
     * 클럽 생성
     */
    public Long save(Club club) {
        if (memberService.findById(club.getLeaderId()).getClub()!=null){
            throw new ApiException(ErrorType.ALREADY_HAVE_GROUP);
        }
        return clubRepository.save(club).getId();
    }

    /**
     * 클럽 가입
     */
    public Club join(Long clubId, Long memberId){
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        Member member = memberService.findById(memberId);
        if (member.getClub()!=null) {
            throw new ApiException(ErrorType.ALREADY_HAVE_GROUP);
        }
        if (club.getNumOfMember() <= club.getJoinedMemberNum()){
            throw new ApiException(ErrorType.CANT_JOIN_GROUP);
        }
        club.addMember(member);
        member.setClub(club);
        return club;
    }

    public List<Member> getMembers(Long id){
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        return club.getMembers();
    }

    /**
     * 클럽 수정
     */
    public Long update(Long id, String clubname, String clubInfo) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        club.update(clubname, clubInfo);
        return id;
    }

    /**
     * 클럽 삭제
     */
    public Long delete(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        for(Member member:club.getMembers()){
            memberService.deleteClub(member.getId());
        }
        club.getMembers().clear();
        clubRepository.delete(club);
        return id;
    }

    /**
     * 클럽 탈퇴
     */
    public Long outClub(Long clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        Member member = memberService.findById(memberId);
        if (member.getClub()==null){
            throw new ApiException(ErrorType.NOT_HAVE_GROUP);
        }
        club.outMember(member);
        member.setClub(null);
        if (club.getJoinedMemberNum() == 0) {
            clubRepository.delete(club);
        }
        return clubId;
    }

    /**
     * 전체 클럽 조회
     */
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    /**
     * 특정 클럽 조회
     */
    public Club findById(Long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
    }

    public Long saveTime(Long id, Long time){
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        club.saveTime(time);
        return club.getTotalStudyTime();
    }

    /**
     * 클럽 회원 전체 오늘 공부시간 조회
     */
    public Long getMemberTodayStudyTime(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.GROUP_NOT_FOUND));
        Long todayTime = 0L;
        for (Member member:club.getMembers()){
            todayTime += studyTimeService.getTodayStudyTime(member.getId());
        }
        return todayTime;
    }

}

