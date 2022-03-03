/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.reconcile;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.select.Selector;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;

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

        logger.fine(() -> "Fetching from start token=" + startToken);
        List<CqlIdentifier> identifiers = new ArrayList<>();
        List<Selector> tokenSelectors = new ArrayList<>();
        for (String col: Arrays.asList("resource_type_id","logical_id","version")) {
            identifiers.add(CqlIdentifier.fromCql(col));
            tokenSelectors.add(Selector.column(col));
        }
        // Scan resources from a previous known point
        final Select statement =
            selectFrom("resource_payloads")
            .function(CqlIdentifier.fromCql("\"token\""), tokenSelectors)
            .column("resource_type_id")
            .column("logical_id")
            .column("version")
            .column("resource_payload_key")
            .whereTokenFromIds(identifiers).isGreaterThanOrEqualTo(literal(this.startToken))
            .limit(1024)
            ;
        
        SimpleStatement simpleStatement = statement.build();
        ResultSet lrResult = session.execute(simpleStatement);
        
        long lastToken = startToken;
        Iterator<Row> it = lrResult.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            lastToken = row.getLong(0);
            ResourceRecord rec = new ResourceRecord(
                row.getInt(1), row.getString(2), row.getInt(3), row.getString(4));
            if (!recordHandler.apply(rec)) {
                logger.info("Handler requested we stop processing before the current fetch has completed");
                lastToken = Long.MIN_VALUE; // flag end
                break;
            }
        }

        // This works as long as limit > 1
        if (lastToken == startToken) {
            // no more data to be read, so we can flag it's the end
            lastToken = Long.MIN_VALUE;
        }

        // Return the last token value we read so that the next read can start
        // at this point+1
        return lastToken;
    }
}