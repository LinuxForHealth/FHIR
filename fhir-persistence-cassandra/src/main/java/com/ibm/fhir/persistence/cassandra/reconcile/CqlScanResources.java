/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.reconcile;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.util.function.Function;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * CQL command to read a FHIR resource stored in Cassandra.
 */
public class CqlScanResources {
    private static final Logger logger = Logger.getLogger(CqlScanResources.class.getName());

    // The marker used to determine the start position for the table scan
    private final long startToken;

    // A function used to process each record that is fetched
    private final Function<ResourceRecord, Boolean> recordHandler;

    /**
     * Public constructor
     * @param startToken
     * @param recordHandler
     */
    public CqlScanResources(long startToken, Function<ResourceRecord, Boolean> recordHandler) {
        this.startToken = startToken;
        this.recordHandler = recordHandler;
    }

    /**
     * Execute the CQL read query and return the Resource for the resourceTypeId, logicalId, version
     * tuple.
     * @param resourceType
     * @param session
     * @return
     */
    public long run(CqlSession session) throws FHIRPersistenceException {

        // Scan resources from a previous known point
        final String tokenColumn = "token(resource_type_id, logical_id, version)";
        final Select statement =
            selectFrom("resource_payloads")
            .column(tokenColumn)
            .column("resource_type_id")
            .column("logical_id")
            .column("version")
            .column("resource_payload_key")
            .whereColumn(tokenColumn).isGreaterThanOrEqualTo(literal(this.startToken))
            .limit(1024)
            ;
        PreparedStatement ps = session.prepare(statement.build());
        final BoundStatement boundStatement = ps.bind();

        long lastToken = startToken;
        ResultSet lrResult = session.execute(boundStatement);
        for (Row row = lrResult.one(); row != null; row = lrResult.one()) {
            ResourceRecord rec = new ResourceRecord(
                row.getInt(0), row.getString(1), row.getInt(2), row.getString(3));
            if (!recordHandler.apply(rec)) {
                logger.info("Handler requested we stop processing before the current fetch has completed");
                break;
            }
        }

        // Return the last token value we read so that the next read can start
        // at this point+1
        return lastToken;
    }
}