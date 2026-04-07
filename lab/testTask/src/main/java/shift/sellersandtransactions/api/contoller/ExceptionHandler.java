package shift.sellersandtransactions.api.contoller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shift.sellersandtransactions.api.dto.ErrorDto;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorDto> handleFileNotFoundException(FileNotFoundException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleFileAlreadyExistsException(FileAlreadyExistsException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.CONFLICT.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }


    public ResponseEntity<ErrorDto> handleException(Exception e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
