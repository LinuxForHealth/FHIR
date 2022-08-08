/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.linuxforhealth.fhir.persistence.cassandra.cql.CqlDataUtil;

/**
 * Reads the current version number of a resource.
 */
public class CqlGetCurrentVersion {
    private static final Logger logger = Logger.getLogger(CqlGetCurrentVersion.class.getName());

    // The resource_type_id key value
    private final int resourceTypeId;

    // The logical_id key value
    private final String logicalId;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     */
    public CqlGetCurrentVersion(int resourceTypeId, String logicalId) {
        CqlDataUtil.safeId(logicalId);
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

        // The resource_versions table is already ordered by version DESC so we don't need 
        // to do any explicit order by to get the most recent version.
        Select statement =
                selectFrom("resource_versions")
                .column("version")
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
