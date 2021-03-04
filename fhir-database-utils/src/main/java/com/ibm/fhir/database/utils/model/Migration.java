/*
 * (C) Copyright IBM Corp. 2020, 2021
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

    /**
     * Migrate from some previous version to the current version of this database object
     *
     * @param priorVersion
     *            the version being migrated from
     * @return a list of statements to be executed sequentially to bring a database object from the prior version to the
     *         current version
     * @implSpec this method should only be executed when the current version > priorVersion and so the steps need not be
     *         idempotent
     */
    public List<IDatabaseStatement> migrateFrom(int priorVersion);
}
