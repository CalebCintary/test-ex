package com.example.test_ex.apicontrollers;

import com.google.gson.JsonObject;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerRest extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        JsonObject error = new JsonObject();
        error.addProperty("error", 100);
        //noinspection ConstantConditions
        error.addProperty("message",
                ex.getMessage().substring(ex.getMessage().indexOf('\'') + 1, ex.getMessage().lastIndexOf('\'')) + " is required");

        return new ResponseEntity<>(error.toString(), HttpStatus.BAD_REQUEST);
    }

}
