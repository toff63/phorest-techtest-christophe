package com.marchal.christophe.phoresttechtest.exception;

public class ImportFileParsingException extends ImportFileException {
    public ImportFileParsingException() {
    }

    public ImportFileParsingException(String message) {
        super(message);
    }

    public ImportFileParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportFileParsingException(Throwable cause) {
        super(cause);
    }

    protected ImportFileParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
