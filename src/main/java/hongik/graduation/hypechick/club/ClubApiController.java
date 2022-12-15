package hongik.graduation.hypechick.club;

import hongik.graduation.hypechick.handler.ApiException;
import hongik.graduation.hypechick.handler.ErrorType;
import hongik.graduation.hypechick.member.Level;
import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ClubApiController {

    private final ClubService clubService;
    private final MemberService memberService;

    /**
     * 전체 클럽 조회
     */
    @GetMapping("/api/clubs")
    public Result clubAll() {
        List<Club> clubs = clubService.findClubs();
        List<ClubDto> collect = clubs.stream()
                .map(c -> new ClubDto(c.getId(),memberService.findById(c.getLeaderId()).getUsername(), c.getLeaderId(), c.getClubName(), c.getClubInfo(), c.getNumOfMember(), c.getJoinedMemberNum(), c.getTotalStudyTime(), c.getCreatedDate()))
                .collect(Collectors.toList());
        return new Result<>(collect);
    }

    /**
     * 클럽 조회
     */
    @GetMapping("/api/clubs/{id}")
    public ClubAllInfo getClub(@PathVariable("id") Long id) {
        Club club = clubService.findById(id);
        List<Member> members = club.getMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTotalStudyTime(),m.getLevel()))
                .collect(Collectors.toList());
        Result<List<MemberDto>> listResult = new Result<>(collect);
        return new ClubAllInfo<Result>(club.getId(), memberService.findById(club.getLeaderId()).getUsername(), club.getLeaderId(),club.getClubName(), club.getClubInfo(), club.getNumOfMember(), club.getJoinedMemberNum(), club.getTotalStudyTime(), clubService.getMemberTodayStudyTime(club.getId()),club.getCreatedDate(), listResult);
    }

    /**
     * 클럽 삭제
     */
    @DeleteMapping("/api/clubs/{id}")
    public Long deleteClub(@PathVariable("id") Long id){
        clubService.delete(id);
        return id;
    }

    /**
     * 클럽 탈퇴
     */
    @GetMapping("/api/clubs/out/{clubId}/{memberId}")
    public Long outClub(@PathVariable Long clubId, @PathVariable Long memberId){
        clubService.outClub(clubId, memberId);
        return memberId;
    }

    /**
     * 클럽 수정
     */
    @PutMapping("/api/clubs/{id}")
    public UpdateClubResponse updateClub(@PathVariable("id") Long id, @RequestBody @Valid UpdateClubRequest request) {
        clubService.update(id, request.getClubName(), request.getClubInfo());
        Club club = clubService.findById(id);
        return new UpdateClubResponse(id, club.getClubName(), club.getClubInfo());
    }

    /**
     * 클럽 생성
     */

    @PostMapping("/api/clubs")
    public CreateClubResponse makeClub(@RequestBody @Valid CreateClubRequest request) {
        Member leader = memberService.findById(request.memberId);
        Club club = Club.builder()
                .clubName(request.getClubName())
                .leaderId(leader.getId())
                .numOfMember(request.getNumOfMember())
                .clubInfo(request.clubInfo)
                .build();
        Long id = clubService.save(club);
        clubService.join(club.getId(), leader.getId());

        return CreateClubResponse.builder()
                .clubId(id)
                .leaderName(leader.getUsername())
                .leaderId(leader.getId())
                .clubName(club.getClubName())
                .numOfMember(club.getNumOfMember())
                .clubInfo(club.getClubInfo())
                .createDate(club.getCreatedDate())
                .build();
    }

    @GetMapping("/api/clubs/{clubId}/{memberId}")
    public JoinClubResponse joinClub(@PathVariable Long clubId, @PathVariable Long memberId){
        Club club = clubService.join(clubId, memberId);
        return new JoinClubResponse(club.getId(), club.getClubName(), memberId);
    }

    @Data
    static class JoinClubResponse {
        private Long clubId;
        private String clubName;
        private Long memberId;

        public JoinClubResponse(Long clubId, String clubName, Long memberId) {
            this.clubId = clubId;
            this.clubName = clubName;
            this.memberId = memberId;
        }
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long memberId;
        private String username;
        private Long studyTime;
        private Level level;
    }

    @Data
    static class CreateClubRequest {
        private Long memberId;
        @NotEmpty
        private String clubName;
        private int numOfMember;
        @NotEmpty
        private String clubInfo;
    }

    @Data
    static class CreateClubResponse {
        private Long clubId;
        @NotEmpty
        private String leaderName;
        private Long leaderId;
        @NotEmpty
        private String clubName;
        private int numOfMember;
        @NotEmpty
        private String clubInfo;
        private LocalDate createDate;

        @Builder
        public CreateClubResponse(Long clubId, String leaderName, Long leaderId, String clubName, int numOfMember, String clubInfo, LocalDate createDate) {
            this.clubId = clubId;
            this.leaderName = leaderName;
            this.leaderId = leaderId;
            this.clubName = clubName;
            this.numOfMember = numOfMember;
            this.clubInfo = clubInfo;
            this.createDate = createDate;
        }
    }

    @Data
    static class UpdateClubRequest {
        @NotEmpty
        private String clubName;
        @NotEmpty
        private String clubInfo;
    }

    @Data
    static class UpdateClubResponse {
        private Long id;
        @NotEmpty
        private String clubName;
        @NotEmpty
        private String clubInfo;

        public UpdateClubResponse(Long id, String clubName, String clubInfo) {
            this.id = id;
            this.clubName = clubName;
            this.clubInfo = clubInfo;
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class ClubDto<T> {
        private Long id;
        private String leaderName;
        private Long leaderId;
        private String clubName;
        private String clubInfo;
        private int numOfMember;
        private int joinedMemberNum;
        private Long totalStudyTime;
        private LocalDate createDate;
    }

    @Data
    @AllArgsConstructor
    static class ClubAllInfo<T> {
        private Long id;
        private String leaderName;
        private Long leaderId;
        private String clubName;
        private String clubInfo;
        private int numOfMember;
        private int joinedMemberNum;
        private Long totalStudyTime;
        private Long todayStudyTime;
        private LocalDate createDate;
        private T members;
    }
}
