package com.ibm.fhir.cql.translator;

import java.io.InputStream;
import java.util.List;

import org.cqframework.cql.elm.execution.Library;

public interface CqlTranslationProvider {
    public enum Option {
        EnableDateRangeOptimization,
        EnableAnnotations,
        EnableLocators,
        EnableResultTypes,
        EnableDetailedErrors,
        DisableListTraversal,
        DisableListDemotion,
        DisableListPromotion,
        EnableIntervalDemotion,
        EnableIntervalPromotion,
        DisableMethodInvocation,
        RequireFromKeyword
    }
    
    public static enum Format { XML, JSON, JXSON, COFFEE }
    
    List<Library> translate(InputStream cql) throws CqlTranslationException;

    List<Library> translate(InputStream cql, List<Option> options) throws CqlTranslationException;

    List<Library> translate(InputStream cql, List<Option> options, Format targetFormat) throws CqlTranslationException;
    
}
