package hongik.graduation.hypechick;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /**
     * 모든 Entity의 상위클래스로 Entity들의 createdDate, ModifiedDate 자동으로 관리
     */

    @CreatedDate
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
