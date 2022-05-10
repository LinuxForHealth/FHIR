/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.remote.index.api.IdentityCache;

/**
 * Preload the cache
 */
public class CacheLoader {
    private final IdentityCache cache;

    /**
     * Public constructor
     * @param cache
     */
    public CacheLoader(IdentityCache cache) {
        this.cache = cache;
    }

    public void apply(Connection connection) throws FHIRPersistenceException {
        final String SQL = "SELECT parameter_name, parameter_name_id FROM parameter_names";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cache.addParameterName(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("fetch parameter names failed", x);
        }
    }
}