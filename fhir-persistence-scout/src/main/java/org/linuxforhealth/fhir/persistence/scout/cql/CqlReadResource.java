/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRJsonParser;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Reads the latest version of a resource
 */
public class CqlReadResource implements ICqlReader<Resource> {
    private static final Logger logger = Logger.getLogger(CqlReadResource.class.getName());
    
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
    public CqlReadResource(String partitionId, int resourceTypeId, String logicalId) {
        CqlDataUtil.safeId(partitionId);
        CqlDataUtil.safeId(logicalId);
        this.partitionId = partitionId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
    }
    
    @Override
    public Resource run(CqlSession session) {
        
        // Firstly, look up the payload_id for the latest version of the resource
        final String CQL_LOGICAL_RESOURCE = ""
                + "SELECT payload_id, current_version, last_modified FROM logical_resources "
                + " WHERE partition_id     = " + partitionId
                + "   AND resource_type_id = " + resourceTypeId
                + "   AND logical_id       = ? ";

        ResultSet lrResult = session.execute(CQL_LOGICAL_RESOURCE);
        Row row = lrResult.one();
        if (row != null) {

            final String CQL_PAYLOAD_CHUNKS = ""
                    + "  SELECT ordinal, chunk "
                    + "    FROM payload_chunks "
                    + "   WHERE partition_id = ? "
                    + "     AND payload_id = ? "
                    + "ORDER BY ordinal ";
        
            ResultSet chunks = session.execute(CQL_PAYLOAD_CHUNKS);
        
            try {
                // Read the result rows as a continuous stream
                InputStream in = new GZIPInputStream(new CqlChunkedPayloadStream(chunks));
                return FHIRParser.parser(Format.JSON).parse(new InputStreamReader(in, StandardCharsets.UTF_8));
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
        } else {
            // resource not found
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Resource not found; partition_id=" + partitionId + ", resourceTypeId=" + resourceTypeId
                    + ", logicalId=" + logicalId);
            }
            return null;
        }
    }
}