package hongik.graduation.hypechick.timer;

import hongik.graduation.hypechick.club.ClubService;
import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TimerApiController {

    private final MemberService memberService;
    private final StudyTimeService studyTimeService;
    private final ClubService clubService;

    @PostMapping("/api/timer/{userId}")
    public TimerResponse update(@PathVariable Long userId, @RequestBody @Valid SetTimerRequest request) {
        studyTimeService.save(userId, request.getTime());
        Member member = memberService.saveTimer(userId, request.getTime());
        memberService.upgradeLevel(member);
        if (member.getClub() != null){
            clubService.saveTime(member.getClub().getId(), request.getTime());
        }
        return new TimerResponse(member.getId(), member.getTotalStudyTime());
    }

    @GetMapping("/api/timer/{userId}")
    public TimerResponse getTodayTime(@PathVariable Long userId){
        Long todayStudyTime = studyTimeService.getTodayStudyTime(userId);
        return new TimerResponse(userId, todayStudyTime);
    }

    @Data
    static class SetTimerRequest {
        @NotNull
        private Long time;
    }

    @Data
    static class TimerResponse {
        @NotNull
        private Long id;
        @NotNull
        private Long time;

        public TimerResponse(Long id, Long time) {
            this.id = id;
            this.time = time;
        }
    }
}
