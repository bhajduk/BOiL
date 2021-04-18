package com.ahbh.transport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoCycleException extends RuntimeException{
    public NoCycleException(){
        super("Application can not find cycle :/ ");
    }
}
