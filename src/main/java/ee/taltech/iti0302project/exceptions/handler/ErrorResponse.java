package ee.taltech.iti0302project.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
    }
}