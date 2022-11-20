package hongik.graduation.hypechick;

import hongik.graduation.hypechick.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {

    }

    @Test
    void 로그인() {

    }

}
