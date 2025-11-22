package com.academiadev.domain.exceptions;

public class EnrollmentException extends BusinessException {
    
    public EnrollmentException(String message) {
        super(message);
    }
    
    public EnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}

