package com.consultorio.handler;

import com.consultorio.exeption.BadRequestException;
import com.consultorio.exeption.BadRequestExceptionDetails;
import com.consultorio.exeption.ExceptionDetails;
import com.consultorio.exeption.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


    @ControllerAdvice
    @Log4j2
    public class RestExeptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestExeption(BadRequestException bre){
            return new ResponseEntity<>(
                    BadRequestExceptionDetails.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.BAD_REQUEST.value())
                            .title("Bad Request Exeption, checkk documentation")
                            .details(bre.getMessage())
                            .developerMessage(bre.getClass().getName())
                            .build(), HttpStatus.BAD_REQUEST);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                MethodArgumentNotValidException exeption, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
            List<FieldError> fieldErrors = exeption.getBindingResult().getFieldErrors();

            String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
            String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
            return new ResponseEntity<>(
                    ValidationExceptionDetails.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.BAD_REQUEST.value())
                            .title("Bad Request Invalid Fields")
                            .details("Check the fild(s) error")
                            .developerMessage(exeption.getClass().getName())
                            .fields(fields)
                            .fildsMessage(fieldsMessage)
                            .build(), HttpStatus.BAD_REQUEST);
        }

        @Override
        protected ResponseEntity<Object> handleExceptionInternal(
                Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

            ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(status.value())
                    .title(ex.getCause().getMessage())
                    .details(ex.getMessage())
                    .developerMessage(ex.getClass().getName())
                    .build();

            return new ResponseEntity<>(exceptionDetails, headers, status);
        }
    }

