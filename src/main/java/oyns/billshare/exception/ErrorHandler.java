package oyns.billshare.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .error(404)
                .message(e.getMessage())
                .status(ErrorState.NOT_FOUND)
                .reason("Искомое значение не обнаружено.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("400 {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .error(400)
                .message(e.getMessage())
                .status(ErrorState.BAD_REQUEST)
                .reason("Некорректный запрос к серверу.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse defaultHandle(final RuntimeException e) {
        log.info("500 {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .error(500)
                .message(e.getMessage())
                .status(ErrorState.INTERNAL_SERVER_ERROR)
                .reason("Проблема на стороне сервера.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .error(409)
                .message(e.getMessage())
                .status(ErrorState.CONFLICT)
                .reason("Некорректное изменение поля.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final HttpClientErrorException.Forbidden e) {
        log.info("403 {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .error(403)
                .message(e.getMessage())
                .status(ErrorState.FORBIDDEN)
                .reason("Вы не можете присоединится к этой группе.")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
