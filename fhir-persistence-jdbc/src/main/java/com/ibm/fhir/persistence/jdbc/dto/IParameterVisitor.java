/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * A visitor passed to the parameter visit method
 */
public interface IParameterVisitor {

    /**
     * Process a string parameter value
     */
    void visit(StringParameter stringParameter) throws FHIRPersistenceException;

    /**
     * Process a reference parameter value
     */
//    void visit(ReferenceParameter stringParameter) throws FHIRPersistenceException;

    /**
     * Process a uri parameter value
     */
//    void visit(UriParameter stringParameter) throws FHIRPersistenceException;

    /**
     * Process a number parameter value
     */
    void visit(NumberParameter numberParameter) throws FHIRPersistenceException;

    /**
     * Process a date parameter value
     */
    void visit(DateParameter dateParameter) throws FHIRPersistenceException;

    /**
     * Process a token parameter value
     */
    void visit(TokenParameter tokenParameter) throws FHIRPersistenceException;

    /**
     * Process a quantity parameter value
     */
    void visit(QuantityParameter quantityParameter) throws FHIRPersistenceException;

    /**
     * Process a location parameter value
     */
    void visit(LocationParameter locationParameter) throws FHIRPersistenceException;
}
