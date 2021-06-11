package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import com.ibm.fhir.cql.engine.converter.FhirTypeConverter;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.BundleType;

public class ParameterConverter {
    
    private static final Logger LOG = Logger.getLogger(ParameterConverter.class.getName());

    private FhirTypeConverter typeConverter;

    public ParameterConverter(FhirTypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }
    
    public Parameter toParameter(String paramName, Map<String, Object> expressionResults) {
        Parameter.Builder returnParameter = Parameter.builder().name(fhirstring(paramName));
        for (Map.Entry<String, Object> entry : expressionResults.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            try {
                if( value instanceof Iterable ) {
                    Iterable<?> iterable = (Iterable<?>) value;
                    iterable.forEach( v -> returnParameter.part( toParameter(v).name(fhirstring(name)).build() ) );
                } else {
                    returnParameter.part(toParameter(value).name(fhirstring(name)).build());
                }
            } catch( NotImplementedException nex ) {
                LOG.warning( "Ignoring not-implemented parameter type" );
            }
        }
        return returnParameter.build();
    }

    public Parameter.Builder toParameter(Object value) {
        Parameter.Builder builder = Parameter.builder();
        toParameter(builder, value);
        return builder;
    }

    @SuppressWarnings("unchecked")
    public Parameter.Builder toParameter(Parameter.Builder p, Object value) {
        if (value != null) {
            Object obj;
            if( value instanceof Iterable ) {
                obj = typeConverter.toFhirTypes((Iterable<Object>)value);
            } else { 
                obj = typeConverter.toFhirType(value);
            }

            if (obj instanceof Element) {
                p.value((Element) obj);
            } else if (obj instanceof Resource) {
                p.resource((Resource) obj);
            } else if (obj instanceof Iterable) {
                Bundle.Builder bundle = Bundle.builder().type(BundleType.COLLECTION);
                
                Iterable<?> it = (Iterable<?>) obj;
                for (Object nestedObj : it) {
                    if( nestedObj instanceof Resource ) {
                        bundle.entry(Bundle.Entry.builder().resource((Resource)nestedObj).build());
                    } else {
                        // TODO - what do we do with a list of primitives?
                        throw new IllegalArgumentException("Only lists of resources are supported");
                    }
                }
                p.resource(bundle.build());
            } else {
                throw new IllegalArgumentException(String.format("Unexpected object of type '%s' produced by fhir type conversion"));
            }
        }
        return p;
    }

    public Object toCql(Parameter p) {
        Object result = null;
        if (p.getValue() != null) {
            Element e = (Element) p.getValue();
            result = typeConverter.toCqlType(e);
        } else if (p.getResource() != null) {
            Resource r = (Resource) p.getResource();
            result = typeConverter.toCqlType(r);
        } else if (p.getPart() != null) {
            result = p.getPart().stream().map(np -> toCql(np)).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException(String.format("Unexpected object of type %s specified as input parameter"));
        }
        return result;
    }
}
