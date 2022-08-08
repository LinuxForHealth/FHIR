/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.citus.CitusTranslator;
import org.linuxforhealth.fhir.database.utils.derby.DerbyTranslator;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresTranslator;

/**
 * Factory class for creating instances of the {@link IDatabaseTranslator}
 * interface
 */
public class DatabaseTranslatorFactory {
    /**
     * Get the translator appropriate for the given database type
     * @return
     */
    public static IDatabaseTranslator getTranslator(DbType type) {
        final IDatabaseTranslator result;

        switch (type) {
        case DERBY:
            result = new DerbyTranslator();
            break;
        case POSTGRESQL:
            result = new PostgresTranslator();
            break;
        case CITUS:
            result = new CitusTranslator();
            break;
        default:
            throw new IllegalStateException("DbType not supported: " + type);
        }

        return result;
    }

}
