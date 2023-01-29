package com.marchal.christophe.phoresttechtest.exception;

public class ImportFileException extends RuntimeException {
    public ImportFileException() {
        super();
    }

    public ImportFileException(String message) {
        super(message);
    }

    public ImportFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportFileException(Throwable cause) {
        super(cause);
    }

    protected ImportFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}