package hongik.graduation.hypechick.member;

import hongik.graduation.hypechick.club.Club;
import hongik.graduation.hypechick.login.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 일반 유저, 관리자 구분

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    private Long totalStudyTime;
    private Long levelUpTime;

    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    private String goal;

    @Builder
    public Member(String username, String email, String password, Role role, Level level, Long studyTime, SocialType socialType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.level = level;
        this.totalStudyTime = studyTime;
        this.levelUpTime = 0L;
        this.socialType = socialType;
    }

    public Member update(String username) {
        this.username = username;
        return this;
    }

    public Long setTime(Long time) {
        this.totalStudyTime += time;
        this.levelUpTime += time;
        return this.id;
    }

    public Long setClub(Club club) {
        this.club = club;
        return club.getId();
    }

    public void setGoal(String goal){
        this.goal = goal;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    /**
     * 멤버 업그레이드 구현하기
     * @return
     */

    public void setLevel(Level level){
        this.level = level;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다");
        } else {
            this.level = nextLevel;
            this.levelUpTime = 0L;
        }
    }
}
