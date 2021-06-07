package com.ibm.fhir.operation.cpg;

import java.util.stream.Collectors;

import com.ibm.fhir.cql.engine.converter.FhirTypeConverter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Element;

public class ParameterConverter {

    private FhirTypeConverter typeConverter;

    public ParameterConverter(FhirTypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    public Parameter.Builder toParameter(Object value) {
        Parameter.Builder builder = Parameter.builder();
        toParameter(builder, value);
        return builder;
    }

    public Parameter.Builder toParameter(Parameter.Builder p, Object value) {
        if (value != null) {
            Object obj = typeConverter.toFhirType(value);

            if (obj instanceof Element) {
                p.value((Element) obj);
            } else if (obj instanceof Resource) {
                p.resource((Resource) obj);
            } else if (obj instanceof Iterable) {
                // TODO - turn a list of resources into a Bundle
                Iterable<?> it = (Iterable<?>) obj;
                for (Object nestedObj : it) {
                    Parameter.Builder nestedParam = Parameter.builder();
                    toParameter(nestedParam, nestedObj);
                    p.part(nestedParam.build());
                }
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
