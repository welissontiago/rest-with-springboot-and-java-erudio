package br.com.welissontiago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequiredObjectisNullException extends RuntimeException {
    public RequiredObjectisNullException() {
        super("It is not allowed to persist a null object");

    }

    public RequiredObjectisNullException(String message) {
        super(message);
    }
}
