package hongik.graduation.hypechick.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ApiException extends RuntimeException {
    private ErrorType errorType;

    public ApiException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
