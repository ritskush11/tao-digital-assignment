package com.tao.digital.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

   @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
   public ResponseEntity<Object> exception(Exception exception) {
      return new ResponseEntity<>("Something went wrong... Please contact to admin", HttpStatus.BAD_REQUEST);
   }
}