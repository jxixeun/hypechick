package hongik.graduation.hypechick.timer;

import hongik.graduation.hypechick.BaseTimeEntity;
import hongik.graduation.hypechick.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
public class StudyTime extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    private Long studyTimeByDate;

    @Builder
    public StudyTime(Member member, Long studyTimeByDate) {
        this.member = member;
        this.studyTimeByDate = studyTimeByDate;
    }
}
