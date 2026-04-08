package shift.sellersandtransactions.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shift.sellersandtransactions.api.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Перехватываем ошибки валидации (когда сработал @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // Достаем сообщение об ошибке из аннотации (например, "Сумма должна быть больше нуля")
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // Собираем и возвращаем наш DTO
        return new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    // Здесь же потом можно добавить перехватчик для RuntimeException или CustomNotFoundException,
    // если мы будем искать по ID, которого нет в базе (вернет 404 Not Found)
}