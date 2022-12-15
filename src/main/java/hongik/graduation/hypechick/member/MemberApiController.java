package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.club.Club;
import hongik.graduation.hypechick.login.SocialType;
import hongik.graduation.hypechick.timer.StudyTimeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    /**
     * 실무에서는 절대로 entity를 외부에 노출하거나 파라미터로 받으면 안된다
     * api는 요청, 응답 모두 DTO를 사용해야 한다
     */

    private final MemberService memberService;
    private final StudyTimeService studyTimeService;
    private final PasswordEncoder passwordEncoder;

    @DeleteMapping("/api/members/{id}")
    public Long deleteMember(@PathVariable("id") Long id) {
        memberService.delete(id);
        return id;
    }

    /**
     * 회원 조회
     */
    @GetMapping("/api/members")
    public Result memberAll() {
        List<Member> findUsers = memberService.findMembers();
        List<MemberDto> collect = findUsers.stream()
                .map(u -> new MemberDto(u.getUsername(), u.getEmail()))
                .collect(Collectors.toList());
        return new Result<>(collect);
    }

    @GetMapping("/api/members/{id}")
    public MemberInfoResponse getUser(@PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        if (member.getClub() != null){
            return MemberInfoResponse.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .socialType(member.getSocialType())
                    .totalStudyTime(member.getTotalStudyTime())
                    .todayStudyTime(studyTimeService.getTodayStudyTime(member.getId()))
                    .level(member.getLevel())
                    .clubId(member.getClub().getId())
                    .goal(member.getGoal())
                    .build();
        }
        return MemberInfoResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .socialType(member.getSocialType())
                .totalStudyTime(member.getTotalStudyTime())
                .todayStudyTime(studyTimeService.getTodayStudyTime(member.getId()))
                .level(member.getLevel())
                .clubId(null)
                .goal(member.getGoal())
                .build();
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        //일단 고객의 이름과 이메일만 반환하도록 함
        private String name;
        private String email;
    }

    @Data
    static class MemberInfoResponse {
        private Long id;
        private String username;
        private String email;
        private SocialType socialType;
        private Long totalStudyTime;
        private Long todayStudyTime;
        private Level level;
        private Long clubId;
        private String goal;

        @Builder
        public MemberInfoResponse(Long id, String username, String email, SocialType socialType, Long totalStudyTime, Long todayStudyTime, Level level, Long clubId, String goal) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.socialType = socialType;
            this.totalStudyTime = totalStudyTime;
            this.todayStudyTime = todayStudyTime;
            this.level = level;
            this.clubId = clubId;
            this.goal = goal;
        }
    }

    /**
     * 회원 등록
     */

    @PostMapping("/api/v1/members")
    public CreateMemberResponse joinMember(@RequestBody @Valid CreateMemberRequest request) {
        Member member = Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .level(Level.BASIC)
                .studyTime(0L)
                .socialType(SocialType.EMAIL)
                .build();
        Long id = memberService.join(member);
        return new CreateMemberResponse(id, member.getUsername(),member.getEmail());
    }

    /**
     * 소셜 회원 등록
     */
    @PostMapping("/api/v1/members/{socialLoginType}")
    public CreateMemberResponse joinSocialMember(@PathVariable String socialLoginType, @RequestBody @Valid CreateSocialMemberRequest request){
        SocialType socialType = SocialType.valueOf(socialLoginType.toUpperCase());
        Member member = Member.builder()
                .username(request.getUsername())
                .email(request.getUid())
                .password(null)
                .role(Role.USER)
                .level(Level.BASIC)
                .studyTime(0L)
                .socialType(socialType)
                .build();
        Long id = memberService.join(member);
        return new CreateMemberResponse(id, member.getUsername(),member.getEmail());
    }

    /**
     * 회원 수정
     */
    @PutMapping("/api/members/{id}")
    public UpdateMemberResponse updateUser(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getUsername());
        Member findUser = memberService.findById(id);
        return new UpdateMemberResponse(id, findUser.getUsername(), findUser.getEmail());
    }

    @PostMapping("/api/members/goal/{id}")
    public UpdateMemberGoalResponse updateUserGoal(@PathVariable Long id, @RequestBody @Valid UpdateMemberGoalRequest updateMemberGoalRequest) {
        memberService.setGoal(id, updateMemberGoalRequest.getGoal());
        return new UpdateMemberGoalResponse(id, memberService.findById(id).getGoal());
    }

    @Data
    static class UpdateMemberGoalRequest {
        @NotEmpty
        private String goal;
    }

    @Data
    static class UpdateMemberGoalResponse {
        private Long id;
        private String goal;

        public UpdateMemberGoalResponse(Long id, String goal) {
            this.id = id;
            this.goal = goal;
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String email;
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Data
    static class CreateSocialMemberRequest {
        @NotEmpty
        private String uid;
        @NotEmpty
        private String username;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        private String username;
        private String email;

        public CreateMemberResponse(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String username;
        private String email;
    }
}
