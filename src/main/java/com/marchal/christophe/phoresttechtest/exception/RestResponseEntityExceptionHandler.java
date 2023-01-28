package com.marchal.christophe.phoresttechtest.exception;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.util.ClassUtils.isAssignable;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Catch Entity validation errors
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        TransactionSystemException transactionSystemException = (TransactionSystemException) ex;
        if (transactionSystemException.getOriginalException() != null &&
                isAssignable(transactionSystemException.getOriginalException().getClass(), RollbackException.class) &&
                transactionSystemException.getOriginalException().getCause() != null &&
                isAssignable(transactionSystemException.getOriginalException().getCause().getClass(), ConstraintViolationException.class)
        ) {
            ConstraintViolationException violationException = (ConstraintViolationException) transactionSystemException.getOriginalException().getCause();
            List<String> errors = violationException.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(transactionSystemException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}