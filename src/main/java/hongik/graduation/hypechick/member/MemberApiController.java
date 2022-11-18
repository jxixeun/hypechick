package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.login.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            return new MemberInfoResponse(member.getId(), member.getUsername(), member.getEmail(),member.getClub().getId());
        }
        return new MemberInfoResponse(member.getId(), member.getUsername(), member.getEmail(), null);
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
        //비번 일단 뺌
        private Long id;
        private String username;
        private String email;
        private Long clubId;

        public MemberInfoResponse(Long id, String username, String email, Long clubId) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.clubId = clubId;
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
                .password(request.getPassword())
                .role(Role.USER)
                .level(Level.BASIC)
                .studyTime(0L)
                .socialType(SocialType.BASIC)
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
    public void updateUserGoal(@PathVariable Long id, @RequestBody @Valid UpdateMemberGoalRequest updateMemberGoalRequest) {
        memberService.setGoal(id, updateMemberGoalRequest.getGoal());
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
