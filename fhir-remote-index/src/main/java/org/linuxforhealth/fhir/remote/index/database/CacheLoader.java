/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.params.model.ResourceTypeValue;
import org.linuxforhealth.fhir.remote.index.cache.IdentityCacheImpl;

/**
 * Preload the cache
 */
public class CacheLoader {
    private final IdentityCacheImpl cache;

    /**
     * Public constructor
     * @param cache
     */
    public CacheLoader(IdentityCacheImpl cache) {
        this.cache = cache;
    }

    /**
     * Read records from the database using the given connection and apply the
     * values to the configured cache object.
     * @param connection
     * @throws FHIRPersistenceException
     */
    public void apply(Connection connection) throws FHIRPersistenceException {
        // load the static list of resource types
        List<ResourceTypeValue> resourceTypes = new ArrayList<>();
        final String SELECT_RESOURCE_TYPES = "SELECT resource_type, resource_type_id FROM resource_types";
        try (PreparedStatement ps = connection.prepareStatement(SELECT_RESOURCE_TYPES)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resourceTypes.add(new ResourceTypeValue(rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("fetch parameter names failed", x);
        }
        cache.init(resourceTypes);

        // also seed the cache with all the parameter_names we know so far
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