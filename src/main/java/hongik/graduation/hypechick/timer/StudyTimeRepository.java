package hongik.graduation.hypechick.timer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {
    List<StudyTime> findByMember_Id(Long memberId);
    List<StudyTime> findByMember_IdAndCreatedDate(Long memberId, LocalDate localDate);
}
