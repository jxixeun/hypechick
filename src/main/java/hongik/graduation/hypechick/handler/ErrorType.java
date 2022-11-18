package hongik.graduation.hypechick.handler;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorType {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "S0002", "존재하지 않는 회원입니다."),
    SECURITY_01(HttpStatus.UNAUTHORIZED, "S0001", "권한이 없습니다."),
    MOT_HAVE_GROUP(HttpStatus.BAD_REQUEST, "S0003", "가입한 그룹이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private String message;



    ErrorType(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ErrorType(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
