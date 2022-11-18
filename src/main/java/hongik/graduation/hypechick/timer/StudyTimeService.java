package hongik.graduation.hypechick.timer;

import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyTimeService {

    private final MemberRepository memberRepository;
    private final StudyTimeRepository studyTimeRepository;

    @Transactional
    public Long save(Long memberId, Long times) {
        Member member = memberRepository.findById(memberId).get();
        StudyTime studyTime = StudyTime.builder()
                .studyTimeByDate(times)
                .member(member)
                .build();
        return studyTimeRepository.save(studyTime).getId();
    }

    /**
     * 회원 전체 공부 기록
     */
    @Transactional(readOnly = true)
    public List<StudyTime> findMemberStudyTime(Long memberId) {
        return studyTimeRepository.findByMember_Id(memberId);
    }

    @Transactional
    public Long getTodayStudyTime(Long memberId){
        List<StudyTime> todayStudyTimes = studyTimeRepository.findByMember_IdAndCreatedDate(memberId, LocalDate.now());
        return todayStudyTimes.stream().mapToLong(StudyTime::getStudyTimeByDate).sum();
    }
}
