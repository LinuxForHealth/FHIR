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
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.common.ResultSetReader;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexMessage;
import org.linuxforhealth.fhir.persistence.params.api.IParamValueCollector;
import org.linuxforhealth.fhir.persistence.params.api.IParamValueProcessor;
import org.linuxforhealth.fhir.persistence.params.model.LogicalResourceValue;
import org.linuxforhealth.fhir.remote.index.kafka.ParamMessageHandler;

/**
 * Parameter message handler for processing remote index messages
 */
public class RemoteIndexMessageHandler extends ParamMessageHandler {
    private static final Logger logger = Logger.getLogger(RemoteIndexMessageHandler.class.getName());

    // the database connection to use
    private final Connection connection;

    // Set if we encounter an error requiring the current transaction to be rolled back
    private boolean rollbackOnly;


    /**
     * Public constructor
     * @param connection
     * @param instanceIdentifier
     * @param maxReadyWaitMs
     * @param paramValueCollector
     * @param paramValueProcessor
     */
    public RemoteIndexMessageHandler(Connection connection, String instanceIdentifier, long maxReadyWaitMs, 
            IParamValueCollector paramValueCollector, IParamValueProcessor paramValueProcessor) {
        super(instanceIdentifier, maxReadyWaitMs, paramValueCollector, paramValueProcessor);
        this.connection = connection;
    }


    @Override
    protected void endTransaction() throws FHIRPersistenceException {
        try {
            if (!this.rollbackOnly) {
                logger.fine("Committing transaction");
                connection.commit();

                // any values from parameter_names, code_systems and common_token_values
                // are now committed to the database, so we can publish their record ids
                // to the shared cache which makes them accessible from other threads 
                paramValueCollector.publishValuesToCache();
            } else {
                // something went wrong...try to roll back the transaction before we close
                // everything
                try {
                    connection.rollback();
                } catch (SQLException x) {
                    // It could very well be that we've lost touch with the database in which case
                    // the rollback will also fail. Not much we can do, although we don't bother
                    // with a stack trace here because it's just more noise for the log file, and
                    // the exception that triggered the rollback is already going to be propagated
                    // and logged.
                    logger.severe("Rollback failed; reason=[" + x.getMessage() + "]");
                }
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("commit failed", x);
        } finally {
            // always clear these maps because otherwise they could grow unbounded. Values
            // are cached by the identityCache
            paramValueCollector.reset();
        }
    }

    @Override
    protected void checkReady(List<RemoteIndexMessage> messages, List<RemoteIndexMessage> okToProcess, List<RemoteIndexMessage> notReady) throws FHIRPersistenceException {
        // Get a list of all the resources for which we can see the current logical resource data.
        // If the resource doesn't yet exist or its version meta doesn't the message
        // then we add to the notReady list. If the resource version meta already
        // exceeds the message, then we'll skip processing altogether because it
        // means that there should be another message in the queue with more
        // up-to-date parameters
        Map<Long,RemoteIndexMessage> messageMap = new HashMap<>();
        Map<String,List<RemoteIndexMessage>> messagesByResourceType = new HashMap<>();
        for (RemoteIndexMessage msg: messages) {
            Long logicalResourceId = msg.getData().getLogicalResourceId();
            messageMap.put(logicalResourceId, msg);

            // split out the messages per resource type because we need to read from xx_logical_resources
            List<RemoteIndexMessage> values = messagesByResourceType.computeIfAbsent(msg.getData().getResourceType(), k -> new ArrayList<>());
            values.add(msg);
        }

        Set<Long> found = new HashSet<>();
        final String checkReadyQuery = buildCheckReadyQuery(messagesByResourceType);
        logger.fine(() -> "check ready query: " + checkReadyQuery);
        try (PreparedStatement ps = connection.prepareStatement(checkReadyQuery)) {
            ResultSet rs = ps.executeQuery();
            // wrap the ResultSet in a reader for easier consumption
            ResultSetReader rsReader = new ResultSetReader(rs);
            while (rsReader.next()) {
                LogicalResourceValue lrv = LogicalResourceValue.builder()
                        .withLogicalResourceId(rsReader.getLong())
                        .withResourceType(rsReader.getString())
                        .withLogicalId(rsReader.getString())
                        .withVersionId(rsReader.getInt())
                        .withLastUpdated(rsReader.getTimestamp())
                        .withParameterHash(rsReader.getString())
                        .build();
                RemoteIndexMessage m = messageMap.get(lrv.getLogicalResourceId());
                if (m == null) {
                    throw new IllegalStateException("query returned a logical resource which we didn't request");
                }

                // Check the values from the database to see if they match
                // the information in the message.
                if (m.getData().getVersionId() == lrv.getVersionId()) {
                    // only process this message if the parameter hash and lastUpdated
                    // times match - which is a good check that we're storing parameters
                    // from the correct transaction. If these don't match, we can simply
                    // say we found the data but don't need to process the message.
                    final Instant dbLastUpdated = lrv.getLastUpdated().toInstant();
                    final Instant msgLastUpdated = m.getData().getLastUpdatedInstant();
                    if (lrv.getParameterHash().equals(m.getData().getParameterHash())
                            && dbLastUpdated.equals(msgLastUpdated)) {
                        okToProcess.add(m);
                    } else {
                        logger.warning("Parameter message must match both parameter_hash and last_updated. Must be from an uncommitted transaction so ignoring: " + m.toString());
                    }
                    found.add(lrv.getLogicalResourceId()); // won't be marked as missing
                } else if (m.getData().getVersionId() > lrv.getVersionId()) {
                    // we can skip processing this record because the database has already
                    // been updated with a newer version. Identify the record as having been
                    // found so we don't keep waiting for it
                    found.add(lrv.getLogicalResourceId());
                }
                // if the version in the database is prior to version in the message we
                // received it means that the server transaction hasn't been committed...
                // so we have to wait just as though it were missing altogether
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "prepare failed: " + checkReadyQuery, x);
            throw new FHIRPersistenceException("prepare query failed");
        }

        if (found.size() < messages.size()) {
            // identify the missing records and add to the notReady list
            for (RemoteIndexMessage m: messages) {
                if (!found.contains(m.getData().getLogicalResourceId())) {
                    notReady.add(m);
                }
            }
        }
    }

    /**
     * Build the check ready query
     * @param messagesByResourceType
     * @return
     */
    private String buildCheckReadyQuery(Map<String,List<RemoteIndexMessage>> messagesByResourceType) {
        // The trouble here is that we'll end up with a unique query for every single
        // batch of messages we process (which the database then need to parse etc).
        // This may introduce scaling issues, in which case we should consider 
        // individual queries for each resource type using bind variables, perhaps
        // going so far as using multiple statements with a power-of-2 number of bind
        // variables. But JDBC doesn't support batching of select statements, so
        // the alternative there would be to insert-as-select into a global temp table
        // and then simply select from that. Fairly straightforward, but a lot more
        // work so only worth doing if we identify contention here.

        StringBuilder select = new StringBuilder();
        // SELECT lr.logical_resource_id, lr.resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash
        //   FROM logical_resources AS lr,
        //        patient_logical_resources AS xlr
        //  WHERE lr.logical_resource_id = xlr.logical_resource_id
        //    AND xlr.logical_resource_id IN (1,2,3,4)
        //  UNION ALL
        // SELECT lr.logical_resource_id, lr.resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash
        //   FROM logical_resources AS lr,
        //        observation_logical_resources AS xlr
        //  WHERE lr.logical_resource_id = xlr.logical_resource_id
        //    AND xlr.logical_resource_id IN (5,6,7)
        boolean first = true;
        for (Map.Entry<String, List<RemoteIndexMessage>> entry: messagesByResourceType.entrySet()) {
            final String resourceType = entry.getKey();
            final List<RemoteIndexMessage> messages = entry.getValue();
            final String inlist = messages.stream().map(m -> Long.toString(m.getData().getLogicalResourceId())).collect(Collectors.joining(","));
            if (first) {
                first = false;
            } else {
                select.append(" UNION ALL ");
            }
            select.append(" SELECT lr.logical_resource_id, '" + resourceType + "' AS resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash ");
            select.append("   FROM logical_resources AS lr, ");
            select.append(resourceType).append("_logical_resources AS xlr ");
            select.append("  WHERE lr.logical_resource_id = xlr.logical_resource_id ");
            select.append("    AND xlr.logical_resource_id IN (").append(inlist).append(")");
        }
        
        return select.toString();
    }

    @Override
    protected void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    @Override
    public void close() {
        // We don't own the connection, so no need to close it here
    }
}