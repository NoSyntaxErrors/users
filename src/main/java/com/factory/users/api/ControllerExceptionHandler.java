package com.factory.users.api;

import com.factory.users.model.ErrorResponse;
import com.factory.users.model.exception.ResourceException;
import com.factory.users.model.exception.UnauthorizedAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
@ComponentScan("com.factory.users.api")
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public ErrorResponse badRequestHttpClientErrorException(HttpClientErrorException e) {

        log.error("ControllerExceptionHandler - HttpClientErrorException", e);

        return getExceptionErrorMessage(e, BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerException(Exception e) {

        log.error("ControllerExceptionHandler - Exception", e);

        return getExceptionErrorMessage(e, INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public ErrorResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        log.error("ControllerExceptionHandler - MethodArgumentTypeMismatchException", e);

        return getExceptionErrorMessage(e, BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("ControllerExceptionHandler - MethodArgumentNotValidException", e);

        return getExceptionErrorMessage(e, BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceException.class})
    @ResponseStatus(value = CONFLICT)
    public ErrorResponse resourceException(ResourceException e) {

        log.error("ControllerExceptionHandler - ResourceException", e);

        return getExceptionErrorMessage(e, CONFLICT);
    }

    @ExceptionHandler(value = {UnauthorizedAccess.class})
    @ResponseStatus(value = UNAUTHORIZED)
    public ErrorResponse unauthorizedAccess(UnauthorizedAccess e) {

        log.error("ControllerExceptionHandler - UnauthorizedAccess", e);

        return getExceptionErrorMessage(e, UNAUTHORIZED);
    }

    private ErrorResponse getExceptionErrorMessage(Exception e, HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .detail(e.getMessage())
                .code(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
