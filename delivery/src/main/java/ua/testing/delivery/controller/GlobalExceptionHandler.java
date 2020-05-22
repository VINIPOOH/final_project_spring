package ua.testing.delivery.controller;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handling() {
        log.debug("");

        return "redirect:404";
    }

//    @ExceptionHandler(Throwable.class)
//    public String unCachedExceptionHandler() {
//        return "405";
//    }
}
