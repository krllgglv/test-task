package ru.kgogolev.app.exceptions;

public class CantSaveFileException extends RuntimeException {
    public CantSaveFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantSaveFileException(String message) {
        super(message);
    }
}
