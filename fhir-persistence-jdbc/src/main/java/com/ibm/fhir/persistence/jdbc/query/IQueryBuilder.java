/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.query;


/**
 * Builds FHIR search queries for the IBM FHIR Server physical data model.
 *
 * Implementations are expected to track table aliasing so that the calling
 * code doesn't need to concern itself.
 */
public interface IQueryBuilder {

}
