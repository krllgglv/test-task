package ru.kgogolev.app.exceptions;

public class FileIsMissingException extends RuntimeException{
    public FileIsMissingException(String message) {
        super(message);
    }

    public FileIsMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
