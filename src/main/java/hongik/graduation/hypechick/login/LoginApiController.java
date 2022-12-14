package hongik.graduation.hypechick.login;

import hongik.graduation.hypechick.club.Club;
import hongik.graduation.hypechick.handler.ApiException;
import hongik.graduation.hypechick.handler.ErrorType;
import hongik.graduation.hypechick.member.Level;
import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.timer.StudyTimeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StudyTimeService studyTimeService;

    @PostMapping("/api/v1/login")
    public SuccessResult<LoginResponse> loginApi(@RequestBody LoginRequest request) {
        Member loginMember = loginService.login(request.getEmail(), request.getPassword());
        log.info("login ? {}", loginMember);
        if (loginMember == null) {
            throw new ApiException(ErrorType.MEMBER_NOT_FOUND);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(loginMember.getId());
        loginResponse.setUsername(loginMember.getUsername());
        loginResponse.setEmail(loginMember.getEmail());
        loginResponse.setSocialType(loginMember.getSocialType());
        loginResponse.setTotalStudyTime(loginMember.getTotalStudyTime());
        loginResponse.setTodayStudyTime(studyTimeService.getTodayStudyTime(loginMember.getId()));
        loginResponse.setLevel(loginMember.getLevel());
        if (loginMember.getClub() != null){
            loginResponse.setClubId(loginMember.getClub().getId());
        }
        else {
            loginResponse.setClubId(null);
        }
        loginResponse.setGoal(loginMember.getGoal());

        return new SuccessResult<>(jwtTokenProvider.createToken(request.getEmail()), loginResponse);
    }

    @PostMapping("/api/v1/socialLogin/{socialLoginType}")
    public SuccessResult<LoginResponse> socialLogin(@PathVariable String socialLoginType, @RequestBody SocialLoginRequest request) {
        SocialType socialType = SocialType.valueOf(socialLoginType.toUpperCase());
        Member loginMember = loginService.socialLogin(socialType, request.getUid());
        if (loginMember == null){
            throw new ApiException(ErrorType.MEMBER_NOT_FOUND);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(loginMember.getId());
        loginResponse.setUsername(loginMember.getUsername());
        loginResponse.setEmail(loginMember.getEmail());
        loginResponse.setSocialType(loginMember.getSocialType());
        loginResponse.setTotalStudyTime(loginMember.getTotalStudyTime());
        loginResponse.setTodayStudyTime(studyTimeService.getTodayStudyTime(loginMember.getId()));
        loginResponse.setLevel(loginMember.getLevel());
        if (loginMember.getClub() != null){
            loginResponse.setClubId(loginMember.getClub().getId());
        }
        else {
            loginResponse.setClubId(null);
        }
        loginResponse.setGoal(loginMember.getGoal());
        return new SuccessResult<>(jwtTokenProvider.createToken(loginMember.getEmail()), loginResponse);
    }

    @Data
    @AllArgsConstructor
    static class SuccessResult<T> {
        private String result;
        private Long expires_in;
        private String token;
        private String message;
        private T user_info;

        public SuccessResult(String token, T user_info) {
            this.result = "success";
            this.expires_in = 72 * 60 * 60 * 1000L;
            this.token = token;
            this.message = "login success";
            this.user_info = user_info;
        }
    }


    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    static class SocialLoginRequest {
        private String uid;
    }

    @Data
    static class LoginResponse {
        private Long id;
        private String username;
        private String email;
        private SocialType socialType;
        private Long totalStudyTime;
        private Long todayStudyTime;
        private Level level;
        private Long clubId;
        private String goal;
    }

    @Data
    static class SocialResponse {
        private String email;
        private String username;
    }

}

