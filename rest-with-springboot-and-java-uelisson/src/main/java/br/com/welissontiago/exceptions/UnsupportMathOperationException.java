package br.com.welissontiago.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportMathOperationException extends RuntimeException {
    public UnsupportMathOperationException(String message) {
        super(message);
    }
}
