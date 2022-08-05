/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Checks the RESOURCE_CHANGE_LOG table to see if it has been populated with
 * any data
 */
public class GetResourceChangeLogEmpty implements IDatabaseSupplier<Boolean> {
    private final String schemaName;

    public GetResourceChangeLogEmpty(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = true;

        final String SQL = "SELECT 1 "
                + "  FROM " + schemaName + ".RESOURCE_CHANGE_LOG "
                + translator.limit("1");

        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            if (rs.next()) {
                result = false;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }

}
