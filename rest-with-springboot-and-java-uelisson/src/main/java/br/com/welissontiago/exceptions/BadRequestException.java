package br.com.welissontiago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Unsupported file format");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
