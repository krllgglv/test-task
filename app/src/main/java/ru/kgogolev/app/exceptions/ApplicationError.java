package ru.kgogolev.app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@AllArgsConstructor
@Data
public class ApplicationError {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

}
