/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.database.utils.model;

import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;

/**
 * A database migration step
 */
public interface Migration {
    public List<IDatabaseStatement> migrateFrom(Integer priorVersion);
}
