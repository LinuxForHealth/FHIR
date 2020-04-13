/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;

/**
 * This No-Db2 Insert interface defines methods for inserting FHIR resource into No-Db2 tables.
 */
public interface NoDB2InsertAPI {
    public long storeResource(String tablePrefix, List<ExtractedParameterValue> parameters, String p_logical_id, byte[] p_payload, Timestamp p_last_updated, boolean p_is_deleted,
            String p_source_key, Integer p_version) throws Exception;
    public int getOrCreateResourceType(String resourceTypeName) throws SQLException;
}
