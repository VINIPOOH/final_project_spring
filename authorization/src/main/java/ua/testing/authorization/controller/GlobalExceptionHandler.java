package ua.testing.authorization.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
@Log4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handling() {
        return "404";
    }

    @ExceptionHandler(Throwable.class)
    public String unCachedExceptionHandler(){
        return "405";
    }
}
