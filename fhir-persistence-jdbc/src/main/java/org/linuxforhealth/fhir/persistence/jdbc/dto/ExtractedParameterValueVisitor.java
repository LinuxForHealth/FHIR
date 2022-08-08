/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * A visitor passed to the parameter visit method
 */
public interface ExtractedParameterValueVisitor {

    /**
     * Process a string parameter value
     */
    void visit(StringParmVal stringParameter) throws FHIRPersistenceException;

    /**
     * Process a uri parameter value
     */
//    void visit(UriParameter stringParameter) throws FHIRPersistenceException;

    /**
     * Process a number parameter value
     */
    void visit(NumberParmVal numberParameter) throws FHIRPersistenceException;

    /**
     * Process a date parameter value
     */
    void visit(DateParmVal dateParameter) throws FHIRPersistenceException;

    /**
     * Process a token parameter value
     */
    void visit(TokenParmVal tokenParameter) throws FHIRPersistenceException;

    /**
     * Process a quantity parameter value
     */
    void visit(QuantityParmVal quantityParameter) throws FHIRPersistenceException;

    /**
     * Process a location parameter value
     */
    void visit(LocationParmVal locationParameter) throws FHIRPersistenceException;

    /**
     * Process a composite parameter value
     */
    void visit(CompositeParmVal compositeParameter) throws FHIRPersistenceException;

    /**
     * Process a reference parameter value
     * @param referenceParmVal
     */
    void visit(ReferenceParmVal referenceParmVal) throws FHIRPersistenceException;
}
