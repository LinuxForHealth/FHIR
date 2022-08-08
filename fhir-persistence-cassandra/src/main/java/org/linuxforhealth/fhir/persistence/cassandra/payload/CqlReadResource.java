/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceSupport;
import org.linuxforhealth.fhir.persistence.cassandra.cql.CqlPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

/**
 * CQL command to read a FHIR resource stored in Cassandra.
 */
public class CqlReadResource {
    private static final Logger logger = Logger.getLogger(CqlReadResource.class.getName());

    // The resource_type_id key value
    private final int resourceTypeId;

    // The logical_id key value
    private final String logicalId;

    // The version for the resource
    private final int version;
    
    // The unique key used to always correctly tie this record to the RDBMS
    private final String resourcePayloadKey;

    // Elements for subsetting the resource during parse
    private final List<String> elements;
    
    // Is the payload compressed when stored
    private final boolean payloadCompressed;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @param elements
     * @param payloadCompressed
     */
    public CqlReadResource(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, List<String> elements, boolean payloadCompressed) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.elements = elements;
        this.payloadCompressed = payloadCompressed;
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

        // Read the resource record, and if the payload was stored in-line, we
        // can process the chunk directly. If chunk is null, we have to read
        // from the payload_chunks table instead
        Select statement =
                selectFrom("resource_payloads")
                .column("chunk")
                .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                .whereColumn("logical_id").isEqualTo(bindMarker())
                .whereColumn("version").isEqualTo(bindMarker())
                .whereColumn("resource_payload_key").isEqualTo(bindMarker())
                .limit(1)
                ;

        PreparedStatement ps = session.prepare(statement.build());

        try {
            ResultSet lrResult = session.execute(ps.bind(logicalId, version, resourcePayloadKey));
            Row row = lrResult.one();
            if (row != null) {
                // If the chunk is small, it's stored in the LOGICAL_RESOURCS record. If
                // it's big, then it requires multiple fetches
                ByteBuffer bb = row.getByteBuffer(0);
                if (bb != null) {
                    // The payload is small enough to fit in the current row
                    InputOutputByteStream readStream = new InputOutputByteStream(bb);
                    result = FHIRPersistenceSupport.parse(resourceType, readStream.inputStream(), this.elements, this.payloadCompressed);
                } else {
                    // Big payload split into multiple chunks. Requires a separate read
                    return readFromChunks(resourceType, session);
                }
            } else {
                // resource doesn't exist.
                result = null;
            }
        } catch (IOException x) {
            logger.log(Level.SEVERE, "Error reading resource resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId);
            throw new CqlPersistenceException("error reading resource", x);
        } catch (FHIRParserException x) {
            // particularly bad...resources are validated before being saved, so if we get a
            // parse failure here that's not IO related, it's not good (database altered or
            // inconsistent?)
            logger.log(Level.SEVERE, "Error parsing resource resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId);
            throw new CqlPersistenceException("parse resource failed", x);
        }

        return result;
    }

    /**
     * Read the resource payload from the payload_chunks table
     * @param resourceType
     * @param session
     * @return
     */
    private <T extends Resource> T readFromChunks(Class<T> resourceType, CqlSession session) throws IOException, FHIRParserException {
        Select statement =
                selectFrom("payload_chunks")
                .column("ordinal")
                .column("chunk")
                .whereColumn("resource_payload_key").isEqualTo(bindMarker())
                .orderBy("ordinal", ClusteringOrder.ASC)
                ;

        PreparedStatement ps = session.prepare(statement.build());
        ResultSet chunks = session.execute(ps.bind(resourcePayloadKey));
        try (InputStream in = new CqlChunkedPayloadStream(new ResultSetBufferProvider(chunks, 1))) {
            return FHIRPersistenceSupport.parse(resourceType, in, this.elements, this.payloadCompressed);
        }
    }
}