package oyns.billshare.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int error;
    private String message;
    private String reason;
    private ErrorState status;
    private LocalDateTime timestamp;
}