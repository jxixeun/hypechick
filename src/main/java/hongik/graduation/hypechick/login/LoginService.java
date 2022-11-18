package hongik.graduation.hypechick.login;

import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberRepository;
import hongik.graduation.hypechick.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * return null이면 로그인 실패
     */
    public Member login(String email, String password) {
        return memberRepository.findByEmail(email).stream()
                .filter(m -> m.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public Member socialLogin(SocialType socialType, String uid) {
        return memberRepository.findBySocialType(socialType).stream()
                .filter(m -> m.getEmail().equals(uid))
                .findFirst()
                .orElse(null);
    }
}

