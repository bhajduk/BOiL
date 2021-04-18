package com.ahbh.transport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInput extends RuntimeException{
    public InvalidInput() {
        super("The entered data is incorrect");
    }

    public InvalidInput(String message) {
        super(message);
    }
}
