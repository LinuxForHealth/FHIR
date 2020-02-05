/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;

public abstract class FHIRPathPatchOperation implements FHIRPatch {
    public static final String TYPE = "type";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String INDEX = "index";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";

    /**
     * Parse the passed Parameter into the appropriate FHIRPathPatchOperation
     * @throws IllegalArgumentException if the Parameter object does not represent a valid FHIRPath Patch operation
     */
    public static FHIRPathPatchOperation parse(Parameter operation) {
        boolean foundType = false, foundPath = false, foundName = false, foundValue = false, foundIndex = false, foundSource = false, foundDestination = false;
        FHIRPatchType type = null;
        String fhirPath = null;
        String name = null;
        Element value = null;
        Integer index = null;
        Integer source = null;
        Integer destination = null;

        for (Parameter part : operation.getPart()) {
            String partName = part.getName().getValue();
            switch (partName) {
            case TYPE:
                Code valueCode = validatePartAndGetValue(foundType, part, Code.class);
                type = FHIRPatchType.from(valueCode.getValue());
                foundType = true;
                break;
            case PATH:
                fhirPath = validatePartAndGetValue(foundPath, part, com.ibm.fhir.model.type.String.class).getValue();
                foundPath = true;
                break;
            case NAME:
                name = validatePartAndGetValue(foundName, part, com.ibm.fhir.model.type.String.class).getValue();
                foundName = true;
                break;
            case VALUE:
                value = validatePartAndGetValue(foundValue, part, Element.class);
                foundValue = true;
                break;
            case INDEX:
                index = validatePartAndGetValue(foundIndex, part, com.ibm.fhir.model.type.Integer.class).getValue();
                foundIndex = true;
                break;
            case SOURCE:
                source = validatePartAndGetValue(foundSource, part, com.ibm.fhir.model.type.Integer.class).getValue();
                foundSource = true;
                break;
            case DESTINATION:
                destination = validatePartAndGetValue(foundDestination, part, com.ibm.fhir.model.type.Integer.class).getValue();
                foundDestination = true;
                break;
            default:
                throw new IllegalArgumentException("Found invalid part with name '" + partName + "'");
            }
        }
        if (type == null) {
            throw new IllegalArgumentException("Missing required part with name 'type'");
        }
        try {
            switch (type) {
            case ADD:       return new FHIRPatchAdd(fhirPath, name, value);
            case DELETE:    return new FHIRPatchDelete(fhirPath);
            case INSERT:    return new FHIRPatchInsert(fhirPath, value, index);
            case MOVE:      return new FHIRPatchMove(fhirPath, source, destination);
            case REPLACE:   return new FHIRPatchReplace(fhirPath, value);
            default:
                throw new IllegalArgumentException("Invalid FHIRPath patch operation type: " + type.name());
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid parameters for patch operation " + type.name(), e);
        }
    }

    private static <T extends Element> T validatePartAndGetValue(boolean alreadyFound, Parameter part, Class<T> valueType) {
        if (alreadyFound) {
            throw new IllegalArgumentException("Part with name='" + part.getName() + "' cannot be repeated");
        }
        if (!part.getValue().is(valueType)) {
            throw new IllegalArgumentException("Part with name='" + part.getName() + "' must be of type " + valueType.getSimpleName());
        }
        return part.getValue().as(valueType);
    }

    /**
     * Infer the element name from a given fhirPath
     * 
     * @param fhirPath
     *            A "simple" fhirpath expression with no functions or operations
     * @return the elementName of the element that the given path would select
     */
    protected String getElementName(String fhirPath) {
        String[] segments = fhirPath.split("\\.");
        String lastSegment = segments[segments.length - 1];
        if (lastSegment.contains("[")) {
            return lastSegment.substring(0, lastSegment.indexOf("["));
        }
        return lastSegment;
    }
}