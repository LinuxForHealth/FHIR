/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.parser;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

public abstract class FHIRAbstractParser implements FHIRParser {
    protected Map<String, Object> properties = new HashMap<>();
    protected boolean validating = true;
    protected boolean ignoringUnrecognizedElements = false;

    @Override
    public abstract <T extends Resource> T parse(InputStream in) throws FHIRParserException;

    @Override
    public abstract <T extends Resource> T parse(Reader reader) throws FHIRParserException;

    @Override
    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    @Override
    public boolean isValidating() {
        return validating;
    }

    @Override
    public void setIgnoringUnrecognizedElements(boolean ignoringUnrecognizedElements) {
        this.ignoringUnrecognizedElements = ignoringUnrecognizedElements;
    }

    @Override
    public boolean isIgnoringUnrecognizedElements() {
        return ignoringUnrecognizedElements;
    }

    @Override
    public void setProperty(String name, Object value) {
        Objects.requireNonNull(name);
        if (!isPropertySupported(name)) {
            throw new IllegalArgumentException("Property: " + name + " is not supported.");
        }
        properties.put(name, Objects.requireNonNull(value));
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(Objects.requireNonNull(name));
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
        return Objects.requireNonNull(type).cast(getProperty(name));
    }

    @Override
    public Object getPropertyOrDefault(String name, Object defaultValue) {
        return properties.getOrDefault(Objects.requireNonNull(name), Objects.requireNonNull(defaultValue));
    }

    @Override
    public <T> T getPropertyOrDefault(String name, T defaultValue, Class<T> type) {
        return Objects.requireNonNull(type).cast(getPropertyOrDefault(name, defaultValue));
    }

    @Override
    public boolean isPropertySupported(String name) {
        return false;
    }

    @Override
    public <T extends FHIRParser> T as(Class<T> parserClass) {
        return parserClass.cast(this);
    }
}
