package ru.kgogolev.app.exceptions.handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import ru.kgogolev.app.exceptions.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchCantReadFileException(CantReadFileException e) {
        return new ResponseEntity<>(new ApplicationError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                LocalDateTime.now()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchCantSaveFileException(CantSaveFileException e) {
        return new ResponseEntity<>(new ApplicationError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                LocalDateTime.now()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchFileIsMissingException(FileIsMissingException e) {
        return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchFileSystemException(FileSystemException e) {
        return new ResponseEntity<>(new ApplicationError(HttpStatus.SERVICE_UNAVAILABLE.value(),
                e.getMessage(),
                LocalDateTime.now()),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchMultipartException(MultipartException e) {
        return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                "Максимальный размер файла для загрузки = 20 Мб",
                LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }


}
