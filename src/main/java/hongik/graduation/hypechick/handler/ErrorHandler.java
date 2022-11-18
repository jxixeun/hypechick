package hongik.graduation.hypechick.handler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, ApiException e){
        return ResponseEntity.status(e.getErrorType().getStatus())
                .body(ErrorResponse.builder()
                        .errorCode(e.getErrorType().getCode())
                        .message(e.getErrorType().getMessage())
                        .build());
    }

    @Data
    static class ErrorResponse {
        String errorCode;
        String message;

        @Builder
        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }
}
