package com.epidemicSimulationSync.epidemicSimulationSync;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomException
{
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity MovieNotFoundHandler(ConstraintViolationException ex) {return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);}
}
