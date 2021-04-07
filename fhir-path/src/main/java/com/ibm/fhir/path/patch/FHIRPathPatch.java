/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;

public class FHIRPathPatch implements FHIRPatch {
    private final List<FHIRPathPatchOperation> operations;

    private FHIRPathPatch(Builder builder) {
        this.operations = Collections.unmodifiableList(builder.operations);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        for (FHIRPathPatchOperation fhirPathPatchOperation : operations) {
            resource = fhirPathPatchOperation.apply(resource);
        }
        return resource;
    }

    /**
     * Convert the FHIRPathPatch to a FHIR Parameters resource
     */
    public Parameters toParameters() {
        Parameters.Builder builder = Parameters.builder().id(UUID.randomUUID().toString());
        for (FHIRPathPatchOperation operation : operations) {
            builder.parameter(operation.toParameter());
        }
        return builder.build();
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<FHIRPathPatchOperation> operations = new ArrayList<>(3);

        private Builder() {
            // hidden constructor
        }

        /**
         * Add an add operation to the FHIRPathPatch
         */
        public Builder add(String path, String elementName, Element element) {
            operations.add(new FHIRPathPatchAdd(path, elementName, element));
            return this;
        }

        /**
         * Add a delete operation to the FHIRPathPatch
         */
        public Builder delete(String path) {
            operations.add(new FHIRPathPatchDelete(path));
            return this;
        }

        /**
         * Add an insert operation to the FHIRPathPatch
         */
        public Builder insert(String path, Element element, Integer index) {
            operations.add(new FHIRPathPatchInsert(path, element, index));
            return this;
        }

        /**
         * Add a move operation to the FHIRPathPatch
         */
        public Builder move(String path, Integer source, Integer destination) {
            operations.add(new FHIRPathPatchMove(path, source, destination));
            return this;
        }

        /**
         * Add an add operation to the FHIRPathPatch
         */
        public Builder replace(String path, Element element) {
            operations.add(new FHIRPathPatchReplace(path, element));
            return this;
        }

        /**
         * Add all patch operations from the passed FHIRPathPatch
         */
        public Builder from(FHIRPathPatch patch) {
            operations.addAll(patch.operations);
            return this;
        }

        /**
         * Build the {@link FHIRPathPatch}
         *
         * @return
         *     An immutable object of type {@link FHIRPathPatch}
         */
        public FHIRPathPatch build() {
            return new FHIRPathPatch(this);
        }
    }

    /**
     * Parse a FHIRPathPatch from a FHIR Parameters resource
     *
     * @throws IllegalArgumentException if the Parameters object does not satisfy the requirements of a FHIRPath Patch
     */
    public static FHIRPathPatch from(Parameters params) {
        Objects.requireNonNull(params);
        Builder builder = FHIRPathPatch.builder();

        for (Parameter param : params.getParameter()) {
            if (!FHIRPathPatchOperation.OPERATION.equals(param.getName().getValue())) {
                throw new IllegalArgumentException("Each FHIRPath patch operation must have a name of 'operation'");
            }
            addOperation(builder, param);
        }

        return builder.build();
    }

    /**
     * Parse the passed Parameter and add it to the builder
     *
     * @throws IllegalArgumentException if the Parameter object does not represent a valid FHIRPath Patch operation
     */
    private static void addOperation(Builder builder, Parameter operation) {
        boolean foundType = false, foundPath = false, foundName = false, foundValue = false, foundIndex = false, foundSource = false, foundDestination = false;
        FHIRPathPatchType type = null;
        String fhirPath = null;
        String name = null;
        Element value = null;
        Integer index = null;
        Integer source = null;
        Integer destination = null;

        for (Parameter part : operation.getPart()) {
            String partName = part.getName().getValue();
            switch (partName) {
            case FHIRPathPatchOperation.TYPE:
                Code valueCode = validatePartAndGetValue(foundType, part, Code.class);
                type = FHIRPathPatchType.from(valueCode.getValue());
                foundType = true;
                break;
            case FHIRPathPatchOperation.PATH:
                fhirPath = validatePartAndGetValue(foundPath, part, com.ibm.fhir.model.type.String.class).getValue();
                foundPath = true;
                break;
            case FHIRPathPatchOperation.NAME:
                name = validatePartAndGetValue(foundName, part, com.ibm.fhir.model.type.String.class).getValue();
                foundName = true;
                break;
            case FHIRPathPatchOperation.VALUE:
                if (part.getValue() == null) {
                    throw new UnsupportedOperationException("Nested value patches are not yet supported");
                }
                value = validatePartAndGetValue(foundValue, part, Element.class);
                foundValue = true;
                break;
            case FHIRPathPatchOperation.INDEX:
                index = validatePartAndGetValue(foundIndex, part, com.ibm.fhir.model.type.Integer.class).getValue();
                foundIndex = true;
                break;
            case FHIRPathPatchOperation.SOURCE:
                source = validatePartAndGetValue(foundSource, part, com.ibm.fhir.model.type.Integer.class).getValue();
                foundSource = true;
                break;
            case FHIRPathPatchOperation.DESTINATION:
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
            case ADD:
                builder.add(fhirPath, name, value);
                break;
            case DELETE:
                builder.delete(fhirPath);
                break;
            case INSERT:
                builder.insert(fhirPath, value, index);
                break;
            case MOVE:
                builder.move(fhirPath, source, destination);
                break;
            case REPLACE:
                builder.replace(fhirPath, value);
                break;
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
}
