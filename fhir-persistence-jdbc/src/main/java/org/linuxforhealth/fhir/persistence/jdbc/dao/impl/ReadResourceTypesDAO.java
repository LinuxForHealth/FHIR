/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.schema.model.ResourceType;

/**
 * A DAO to read all the resource types from the RESOURCE_TYPES table
 * in the current schema
 */
public class ReadResourceTypesDAO implements IDatabaseSupplier<List<ResourceType>> {

    @Override
    public List<ResourceType> run(IDatabaseTranslator translator, Connection c) {
        List<ResourceType> result = new ArrayList<>();

        final String SQL = ""
                + "SELECT resource_type_id, resource_type "
                + "  FROM RESOURCE_TYPES";

        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            while (rs.next()) {
                ResourceType rt = new ResourceType();
                rt.setId(rs.getInt(1));
                rt.setName(rs.getString(2));
                result.add(rt);
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }
}