/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cpg;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import org.linuxforhealth.fhir.cql.engine.converter.FHIRTypeConverter;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.code.BundleType;

/**
 * Provide conversion logic for FHIR Parameter objects both to and from the CQL System
 * types. This relies on data type conversion logic from the <code>FHIRTypeConverter</code>
 * and then is further responsible for encoding those conversions in the needed
 * Parameter format.
 */
public class ParameterConverter {
    private FHIRTypeConverter typeConverter;

    /**
     * Initialize with the given data type conversion logic.
     * 
     * @param typeConverter
     *            FHIR data type conversion logic
     */
    public ParameterConverter(FHIRTypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }
    
    /**
     * Convert CQL parameters into a FHIR Parameter resource
     * 
     * @param paramName
     *            Name of the FHIR parameter to generate
     * @param expressionResults
     *            CQL types to convert
     * @return FHIR Parameter resource
     */
    public Parameter toParameter(String paramName, Map<String, Object> expressionResults) {
        Parameter.Builder returnParameter = Parameter.builder().name(fhirstring(paramName));
        for (Map.Entry<String, Object> entry : expressionResults.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            if( value != null && value instanceof Iterable ) {
                Iterable<?> iterable = (Iterable<?>) value;
                if( iterable.iterator().hasNext() ) {
                    // TODO - this is non-atomic in that a nested Tuple would cause an incomplete return value
                    iterable.forEach(v -> returnParameter.part(toParameter(v).name(fhirstring(name)).build()));
                } else { 
                    // this is what the cqf-ruler implementation does, though it is kind of funky
                    returnParameter.part(toParameter(fhirstring(iterable.toString())).name(fhirstring(name)).build());
                }
            } else {
                returnParameter.part(toParameter(value).name(fhirstring(name)).build());
            }
        }
        return returnParameter.build();
    }

    /**
     * Convert CQL value to partial FHIR parameter
     * 
     * @param value
     *            CQL value
     * @return partial FHIR parameter
     */
    public Parameter.Builder toParameter(Object value) {
        Parameter.Builder builder = Parameter.builder();
        toParameter(builder, value);
        return builder;
    }

    /**
     * Add a CQL value to a FHIR parameter that is being built. FHIR BackboneElement
     * types are not supported as target values and will fail. Iterable results that
     * are lists of resources are automatically converted into FHIR bundle resources in
     * the generated parameter. Lists of primitive values as results are not currently
     * supported.
     * 
     * @param value
     *            CQL value
     * @return partial FHIR parameter with added value
     */
    @SuppressWarnings("unchecked")
    public Parameter.Builder toParameter(Parameter.Builder p, Object value) {
        if (value != null) {
            Object obj;
            
            try { 
                if( value instanceof Iterable ) {
                    obj = typeConverter.toFhirTypes((Iterable<Object>)value);
                } else { 
                    obj = typeConverter.toFhirType(value);
                }
            } catch(NotImplementedException nex) { 
                obj = fhirstring( nex.getMessage() );
            }

            if (obj instanceof BackboneElement ) {
                throw new IllegalArgumentException("Backbone elements are not supported for FHIR parameter conversion");
            } else if (obj instanceof Element ) {
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
        } else { 
            p.value(fhirstring("null"));
        }
        return p;
    }

    /**
     * Convert FHIR parameter contents to a CQL value
     * 
     * @param value
     *            FHIR parameter
     * @return CQL value
     */
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
            throw new IllegalArgumentException(String.format("Parameter %s contained no non-null data elements", p.getName().getValue()));
        }
        return result;
    }
}
