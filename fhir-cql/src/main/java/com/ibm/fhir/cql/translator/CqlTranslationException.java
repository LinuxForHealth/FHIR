package com.ibm.fhir.cql.translator;

import java.util.List;

public class CqlTranslationException extends RuntimeException {

    private static final long serialVersionUID = 16518219151102500L;
    private List<?> errors;

    public CqlTranslationException(String message) {
        super(message);
    }
    
    public CqlTranslationException(String message, Exception cause) {
        super(message, cause);
    }
    
    public CqlTranslationException(Exception cause) {
        super(cause);
    }
    
    public CqlTranslationException(String message, List<?> errors) {
        super(message);
        this.errors = errors;
    }
    
    public CqlTranslationException(String message, Exception cause, List<?> errors) {
        super(message, cause);
        this.errors = errors;
    }
    
    public CqlTranslationException(Exception cause, List<?> errors) {
        super(cause);
        this.errors = errors;
    }
    
    public List<?> getErrors() {
        return errors;
    }
}
