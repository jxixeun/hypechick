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
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"S0003", "이미 가입된 이메일/소셜 계정입니다."),
    CANT_JOIN_GROUP(HttpStatus.BAD_REQUEST,"G0001", "인원이 초과해 가입할 수 없습니다."),
    NOT_HAVE_GROUP(HttpStatus.BAD_REQUEST, "G0002", "가입한 그룹이 없습니다."),
    ALREADY_HAVE_GROUP(HttpStatus.BAD_REQUEST, "G0003", "이미 가입한 그룹이 있습니다."),
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "G0004", "존재하지 않는 그룹입니다."),
    ALREADY_IN_GROUP(HttpStatus.BAD_REQUEST, "G0005", "이미 가입된 그룹입니다."),
    LEVEL_NOT_EXIST(HttpStatus.BAD_REQUEST, "L0001", "존재하지 않는 레벨입니다.");

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
