package hongik.graduation.hypechick.club;

import hongik.graduation.hypechick.BaseTimeEntity;
import hongik.graduation.hypechick.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CLUB_ID")
    private Long id;

    @Column(nullable = false)
    private String clubName;

    private Long leaderId;

    private int numOfMember; // 가능한 멤버 수
    private Long joinedMemberNum; // 가입한 멤버 수

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    private String clubInfo;
    private Long totalStudyTime;

    @Builder
    public Club(String clubName, Long leaderId, int numOfMember, String clubInfo){
        this.clubName = clubName;
        //this.leader = leader;
        this.leaderId = leaderId;
        this.numOfMember = numOfMember;
        this.clubInfo = clubInfo;
        this.joinedMemberNum = 1L;
        this.totalStudyTime = 0L;
    }

    public Club update(String clubName, String clubInfo){
        this.clubName = clubName;
        this.clubInfo = clubInfo;
        return this;
    }

    public Long addMember(Member member){
        members.add(member);
        return member.getId();
    }

    public Long saveTime(Long time){
        this.totalStudyTime += time;
        return this.totalStudyTime;
    }
}
