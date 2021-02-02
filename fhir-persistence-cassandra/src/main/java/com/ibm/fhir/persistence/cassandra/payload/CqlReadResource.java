/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cassandra.cql.CqlDataUtil;
import com.ibm.fhir.persistence.cassandra.cql.CqlPersistenceException;

/**
 * Reads the current version number of a resource. This is used
 * during ingestion so that we can track version history.
 */
public class CqlReadResource {
    private static final Logger logger = Logger.getLogger(CqlReadResource.class.getName());

    // The partition_id key value
    private final String partitionId;

    // The resource_type_id key value
    private final int resourceTypeId;

    // The logical_id key value
    private final String logicalId;

    // The version for the resource
    private final int version;

    /**
     * Public constructor
     * @param partitionId
     * @param resourceTypeId
     * @param logicalId
     */
    public CqlReadResource(String partitionId, int resourceTypeId, String logicalId, int version) {
        CqlDataUtil.safeId(partitionId);
        CqlDataUtil.safeId(logicalId);
        this.partitionId = partitionId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
    }

    /**
     * Execute the CQL read query and return the Resource for the resourceTypeId, logicalId, version
     * tuple.
     * @param session
     * @return
     */
    public <T extends Resource> T run(CqlSession session) {
        // Firstly, look up the payload_id for the latest version of the resource. The table
        // is already ordered by version DESC so we don't need to do any explicit order by to
        // get the most recent version. Simply picking the first row which matches the given
        // resourceType/logicalId is sufficient.
        Select statement =
                selectFrom("logical_resources")
                .column("payload_id")
                .column("chunk")
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                .whereColumn("logical_id").isEqualTo(bindMarker())
                .whereColumn("version").isEqualTo(bindMarker())
                .limit(1)
                ;

        PreparedStatement ps = session.prepare(statement.build());

        try {
            ResultSet lrResult = session.execute(ps.bind(logicalId, version));
            Row row = lrResult.one();
            if (row != null) {
                // If the chunk is small, it's stored in the LOGICAL_RESOURCS record. If
                // it's big, then it requires multiple fetches
                String payloadId = row.getString(1);
                if (payloadId != null) {
                    // Big payload split into multiple chunks. Requires a separate read
                    return readFromChunks(session, payloadId);
                } else {
                    // The payload is small enough to fit in the current row, so no need for an
                    // extra read
                    ByteBuffer bb = row.getByteBuffer(2);
                    InputStream in = new GZIPInputStream(new CqlPayloadStream(bb));
                    return FHIRParser.parser(Format.JSON).parse(new InputStreamReader(in, StandardCharsets.UTF_8));
                }
            } else {
                // resource doesn't exist.
                return null;
            }
        } catch (IOException x) {
            logger.log(Level.SEVERE, "Error reading resource partition_id=" + partitionId + ", resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId);
            throw new CqlPersistenceException("error reading resource", x);
        } catch (FHIRParserException x) {
            // particularly bad...resources are validated before being saved, so if we get a
            // parse failure here that's not IO related, it's not good (database altered?)
            logger.log(Level.SEVERE, "Error parsing resource partition_id=" + partitionId + ", resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId);
            throw new CqlPersistenceException("parse resource failed", x);
        }
    }

    /**
     * @param session
     * @param payloadId
     * @return
     */
    private <T extends Resource> T readFromChunks(CqlSession session, String payloadId) throws IOException, FHIRParserException {
        Select statement =
                selectFrom("payload_chunks")
                .column("chunk")
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("payload_id").isEqualTo(bindMarker())
                .orderBy("ordinal", ClusteringOrder.ASC)
                ;

        PreparedStatement ps = session.prepare(statement.build());
        ResultSet chunks = session.execute(ps.bind(logicalId, version));
        InputStream in = new GZIPInputStream(new CqlChunkedPayloadStream(chunks));
        return FHIRParser.parser(Format.JSON).parse(new InputStreamReader(in, StandardCharsets.UTF_8));
    }
}