/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.parser;

import java.io.InputStream;
import java.io.Reader;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * Parse FHIR resource representations into fhir-model objects
 */
public interface FHIRParser {
    /**
     * Read a resource from the passed InputStream. This method does not close the passed InputStream.
     *
     * @param <T> The resource type to read
     * @param in
     * @return
     * @throws FHIRParserException if the resource could not be parsed for any reason
     */
    <T extends Resource> T parse(InputStream in) throws FHIRParserException;

    /**
     * Read a resource using the passed Reader. This method does not close the passed Reader.
     *
     * @param <T> The resource type to read
     * @param reader
     * @return
     * @throws FHIRParserException if the resource could not be parsed for any reason
     */
    <T extends Resource> T parse(Reader reader) throws FHIRParserException;

    /**
     * Set the validating parser indicator for this parser
     *
     * <p>A validating parser performs basic validation during parsing / deserialization of the input format including:
     * <ul>
     * <li>element cardinality checking</li>
     * <li>element type checking</li>
     * <li>element value checking</li>
     * </ul>
     *
     * @param validating
     *     the validating parser indicator
     * @see ValidationSupport
     */
    void setValidating(boolean validating);

    /**
     * Indicates whether this parser is a validating parser
     *
     * @return
     *     true if this parser is a validating parser, false otherwise
     */
    boolean isValidating();

    /**
     * Set the ignoring unrecognized elements indicator for this parser
     *
     * @param ignoringUnrecognizedElements
     *     the ignoring unrecognized elements indicator
     */
    void setIgnoringUnrecognizedElements(boolean ignoringUnrecognizedElements);

    /**
     * Indicates whether this parser is ignoring unrecognized elements
     *
     * @return
     *     true if this parser is ignoring unrecognized elements, false otherwise
     */
    boolean isIgnoringUnrecognizedElements();

    /**
     * Set the property with the given name to the passed value
     *
     * @throws IllegalArgumentException if the property {@code name} is unknown or unsupported
     * @see {@link #isPropertySupported(String)}
     */
    void setProperty(String name, Object value);

    /**
     * @return the property value or {@code null} if the property has no value
     */
    Object getProperty(String name);

    /**
     * @return the property value or {@code defaultValue} if the property has no value
     */
    Object getPropertyOrDefault(String name, Object defaultValue);

    /**
     * @return the property value or {@code null} if the property has no value
     */
    <T> T getProperty(String name, Class<T> type);

    /**
     * @return the property value or {@code defaultValue} if the property has no value
     */
    <T> T getPropertyOrDefault(String name, T defaultValue, Class<T> type);

    /**
     * Whether the generator supports the property with the passed name
     */
    boolean isPropertySupported(String name);

    /**
     * Attempt to cast the FHIRParser to a specific subclass
     *
     * @param <T> The FHIRParser subclass to cast to
     * @return
     * @throws ClassCastException If the InputStream contains a FHIR resource type that cannot be cast to the requested type
     */
    <T extends FHIRParser> T as(Class<T> parserClass);

    /**
     * Create a FHIRParser for the given format.
     *
     * @param format
     * @return
     * @throws IllegalArgumentException if {@code format} is not supported
     */
    static FHIRParser parser(Format format) {
        switch (format) {
        case JSON:
            return new FHIRJsonParser();
        case XML:
            return new FHIRXMLParser();
        case RDF:
        default:
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}
