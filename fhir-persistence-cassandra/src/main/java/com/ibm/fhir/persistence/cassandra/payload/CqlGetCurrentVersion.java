/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.ibm.fhir.persistence.cassandra.cql.CqlDataUtil;

/**
 * Reads the current version number of a resource. This is used
 * during ingestion so that we can track version history.
 */
public class CqlGetCurrentVersion {
    private static final Logger logger = Logger.getLogger(CqlGetCurrentVersion.class.getName());

    // The partition_id key value
    private final String partitionId;

    // The resource_type_id key value
    private final int resourceTypeId;

    // The logical_id key value
    private final String logicalId;

    /**
     * Public constructor
     * @param partitionId
     * @param resourceTypeId
     * @param logicalId
     */
    public CqlGetCurrentVersion(String partitionId, int resourceTypeId, String logicalId) {
        CqlDataUtil.safeId(partitionId);
        CqlDataUtil.safeId(logicalId);
        this.partitionId = partitionId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
    }

    /**
     * Run the command using the given session
     * @param session
     * @return
     */
    public int run(CqlSession session) {
        int result;

        // Firstly, look up the payload_id for the latest version of the resource. The table
        // is already ordered by version DESC so we don't need to do any explicit order by to
        // get the most recent version. Simply picking the first row which matches the given
        // resourceType/logicalId is sufficient.
        Select statement =
                selectFrom("logical_resources")
                .column("version")
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                .whereColumn("logical_id").isEqualTo(bindMarker())
                .limit(1)
                ;

        PreparedStatement ps = session.prepare(statement.build());

        ResultSet lrResult = session.execute(ps.bind(logicalId));
        Row row = lrResult.one();
        if (row != null) {
            result = row.getInt(1);
        } else {
            result = -1;
        }

        return result;
    }
}
