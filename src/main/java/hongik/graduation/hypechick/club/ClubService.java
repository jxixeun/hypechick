package hongik.graduation.hypechick.club;

import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberService;
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

    /**
     * 클럽 생성
     */
    public Long save(Club club) {
        return clubRepository.save(club).getId();
    }

    public Long join(Long id, Member member){
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
        if (club.getNumOfMember() > club.getJoinedMemberNum()){
            club.addMember(member);
        }
        return club.getId();
    }

    /**
     * 클럽 수정
     */
    public Long update(Long id, String clubname, String clubInfo) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
        club.update(clubname, clubInfo);
        return id;
    }

    /**
     * 클럽 삭제
     */
    public Long delete(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
        for(Member member:club.getMembers()){
            memberService.deleteClub(member.getId());
        }
        club.getMembers().clear();
        clubRepository.delete(club);
        return id;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
    }

    public Long saveTime(Long id, Long time){
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
        club.saveTime(time);
        return club.getTotalStudyTime();
    }

}

