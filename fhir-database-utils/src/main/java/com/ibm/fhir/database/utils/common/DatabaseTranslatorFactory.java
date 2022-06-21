/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.citus.CitusTranslator;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;

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
        case DB2:
            result = new Db2Translator();
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
