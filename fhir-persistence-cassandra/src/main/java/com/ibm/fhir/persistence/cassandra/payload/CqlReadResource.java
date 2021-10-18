/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
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
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.cassandra.cql.CqlDataUtil;
import com.ibm.fhir.persistence.cassandra.cql.CqlPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants;

/**
 * CQL command to read a FHIR resource stored in Cassandra.
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

    private final List<String> elements;

    /**
     * Public constructor
     * @param partitionId
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param elements
     */
    public CqlReadResource(String partitionId, int resourceTypeId, String logicalId, int version, List<String> elements) {
        CqlDataUtil.safeId(partitionId);
        CqlDataUtil.safeId(logicalId);
        this.partitionId = partitionId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.elements = elements;
    }

    /**
     * Execute the CQL read query and return the Resource for the resourceTypeId, logicalId, version
     * tuple.
     * @param resourceType
     * @param session
     * @return
     */
    public <T extends Resource> T run(Class<T> resourceType, CqlSession session) throws FHIRPersistenceException {
        T result;

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
                    return readFromChunks(resourceType, session, payloadId);
                } else {
                    // The payload is small enough to fit in the current row, so no need for an
                    // extra read
                    ByteBuffer bb = row.getByteBuffer(2);
                    try (InputStream in = new GZIPInputStream(new CqlPayloadStream(bb))) {
                        result = parseStream(resourceType, in);
                    }
                }
            } else {
                // resource doesn't exist.
                result = null;
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

        return result;
    }

    /**
     * Read the resource payload from the payload_chunks table
     * @param resourceType
     * @param session
     * @param payloadId
     * @return
     */
    private <T extends Resource> T readFromChunks(Class<T> resourceType, CqlSession session, String payloadId) throws IOException, FHIRParserException {
        Select statement =
                selectFrom("payload_chunks")
                .column("chunk")
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("payload_id").isEqualTo(bindMarker())
                .orderBy("ordinal", ClusteringOrder.ASC)
                ;

        PreparedStatement ps = session.prepare(statement.build());
        ResultSet chunks = session.execute(ps.bind(logicalId, version));
        try (InputStream in = new GZIPInputStream(new CqlChunkedPayloadStream(chunks))) {
            return parseStream(resourceType, in);
        }
    }

    /**
     * Parse the input stream, processing for elements if needed
     * @param <T>
     * @param resourceType
     * @param in
     * @return
     * @throws IOException
     * @throws FHIRParserException
     */
    private <T extends Resource> T parseStream(Class<T> resourceType, InputStream in) throws IOException, FHIRParserException {
        T result;
        if (elements != null) {
            // parse/filter the resource using elements
            result = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parseAndFilter(in, elements);
            if (resourceType.equals(result.getClass()) && !FHIRUtil.hasTag(result, SearchConstants.SUBSETTED_TAG)) {
                // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                result = FHIRUtil.addTag(result, SearchConstants.SUBSETTED_TAG);
            }
        } else {
            result = FHIRParser.parser(Format.JSON).parse(in);
        }

        return result;
    }
}