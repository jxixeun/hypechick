package hongik.graduation.hypechick.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import hongik.graduation.hypechick.BaseTimeEntity;
import hongik.graduation.hypechick.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Tasks extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, columnDefinition = "TEXT", nullable = false)
    private String contents;

    private Boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member; //작성 회원

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    //==생성 메소드==//
    @Builder
    public Tasks(Member member, String contents, LocalDate date) {
        this.member = member;
        this.contents = contents;
        this.isChecked = false;
        this.date = date;
    }

    public void check() {
        Boolean status = this.getIsChecked();
        this.isChecked = !status;
    }

    public void update(String contents) {
        this.contents = contents;
    }
}

