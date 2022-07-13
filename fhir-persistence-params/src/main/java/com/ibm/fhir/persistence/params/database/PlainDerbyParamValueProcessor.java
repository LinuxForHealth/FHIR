/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;
import com.ibm.fhir.persistence.params.api.ParamSchemaConstants;
import com.ibm.fhir.persistence.params.api.ParameterNameDAO;
import com.ibm.fhir.persistence.params.model.CodeSystemValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValueKey;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValueKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;

/**
 * Derby variant of the plain schema message handler which is needed because Derby
 * needs slightly different syntax for some queries
 */
public class PlainDerbyParamValueProcessor extends PlainParamValueProcessor {
    private static final Logger logger = Logger.getLogger(PlainDerbyParamValueProcessor.class.getName());

    /**
     * Public constructor
     * @param connection
     * @param schemaName
     * @param cache
     */
    public PlainDerbyParamValueProcessor(Connection connection, String schemaName, IParameterIdentityCache cache) {
        super(new DerbyTranslator(), connection, schemaName, cache);
    }

    @Override
    protected String onConflict() {
        return "";
    }

    @Override
    protected Integer createParameterName(String parameterName) throws FHIRPersistenceException {
        try {
            final ParameterNameDAO pnd = new DerbyParameterNamesDAO(connection, schemaName);
            return pnd.readOrAddParameterNameId(parameterName);
        } catch (Exception x) {
            logger.log(Level.SEVERE, "add parameter failed", x);
            throw new FHIRPersistenceException("add parameter failed for '" + parameterName + "'");
        }
    }

    @Override
    protected List<LogicalResourceIdentValue> fetchLogicalResourceIdentIds(List<LogicalResourceIdentValue> unresolved, Map<LogicalResourceIdentKey, LogicalResourceIdentValue> logicalResourceIdentMap) throws FHIRPersistenceException {
        // For Derby, we get deadlocks when selecting using the in-list method (see parent implementation
        // of this method). Instead, we execute individual statements in the order of the logical_id
        // list so that the (S) locks will be acquired in the same order as the (X) locks obtained when
        // inserting.
        List<LogicalResourceIdentValue> missing = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT logical_resource_id ");
        query.append("  FROM logical_resource_ident ");
        query.append(" WHERE resource_type_id = ? ");
        query.append("   AND logical_id = ?");

        final String select = query.toString();
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            for (LogicalResourceIdentValue lr: unresolved) {
                ps.setInt(1, lr.getResourceTypeId());
                ps.setString(2, lr.getLogicalId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lr.setLogicalResourceId(rs.getLong(1));
                } else {
                    // entry not found in the database
                    missing.add(lr);
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "logical resource ident fetch failed", x);
            throw new FHIRPersistenceException("logical resource ident fetch failed");
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    @Override
    protected void addMissingLogicalResourceIdents(List<LogicalResourceIdentValue> missing) throws FHIRPersistenceException {
        // for Derby, handle concurrency by catching duplicate values and ignoring - the
        // value will be resolved in the next fetch
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO logical_resource_ident (resource_type_id, logical_id, logical_resource_id) VALUES (?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");

        // To make duplicate handling easier, we execute each statement individually
        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            for (LogicalResourceIdentValue value: missing) {
                ps.setInt(1, value.getResourceTypeId());
                ps.setString(2, value.getLogicalId());
                try {
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (translator.isDuplicate(x)) {
                        // concurrency: ignore because another thread created this just before us
                    } else {
                        throw x;
                    }
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "logical_resource_ident insert failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("logical_resource_ident insert failed");
        }
    }

    @Override
    protected List<CodeSystemValue> fetchCodeSystemIds(List<CodeSystemValue> unresolved, Map<String, CodeSystemValue> codeSystemValueMap) throws FHIRPersistenceException {
        // For Derby, we get deadlocks when selecting using the in-list method (see parent implementation
        // of this method). Instead, we execute individual statements in the order of the unresolved
        // list so that the (S) locks will be acquired in the same order as the (X) locks obtained when
        // inserting.
        List<CodeSystemValue> missing = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT code_system_id ");
        query.append("  FROM code_systems ");
        query.append(" WHERE code_system_name = ?");
        
        final String select = query.toString();
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            for (CodeSystemValue csv: unresolved) {
                ps.setString(1, csv.getCodeSystem());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    csv.setCodeSystemId(rs.getInt(1));
                } else {
                    // entry not found in the database
                    missing.add(csv);
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "code systems fetch failed. SQL=[" + select + "]", x);
            throw new FHIRPersistenceException("code systems fetch failed");
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    @Override
    protected void addMissingCodeSystems(List<CodeSystemValue> codeSystems) {
        // For Derby, do this row-by-row so we can handle concurrency issues
        final String nextVal = translator.nextValue(schemaName, "fhir_ref_sequence");
        final String INS = ""
                + "INSERT INTO code_systems (code_system_id, code_system_name) "
                + "     VALUES (" + nextVal + ", ?)";
        try (PreparedStatement ps = connection.prepareStatement(INS)) {
            for (CodeSystemValue codeSystem: codeSystems) {
                ps.setString(1, codeSystem.getCodeSystem());
                
                try {
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (translator.isDuplicate(x)) {
                        // ignore because this row has already been inserted by another thread
                    } else {
                        throw x;
                    }
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS, x);
            throw translator.translate(x);
        }
    }

    @Override
    protected List<CommonTokenValue> fetchCommonTokenValueIds(List<CommonTokenValue> unresolved, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException {
        // For Derby, we get deadlocks when selecting using the in-list method (see parent implementation
        // of this method). Instead, we execute individual statements in the order of the unresolved
        // list so that the (S) locks will be acquired in the same order as the (X) locks obtained when
        // inserting.
        List<CommonTokenValue> missing = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT c.common_token_value_id ");
        query.append("  FROM common_token_values c ");
        query.append(" WHERE c.code_system_id = ? ");
        query.append("   AND c.token_value = ? ");
        
        final String select = query.toString();
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            for (CommonTokenValue ctv: unresolved) {
                ps.setLong(1, ctv.getCodeSystemValue().getCodeSystemId()); // must be resolved already
                ps.setString(2, ctv.getTokenValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ctv.setCommonTokenValueId(rs.getLong(1));
                } else {
                    // entry not found in the database
                    missing.add(ctv);
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "common token values fetch failed. SQL=[" + select + "]", x);
            throw new FHIRPersistenceException("common token values fetch failed");
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    @Override
    protected void addMissingCommonTokenValues(List<CommonTokenValue> missing) throws FHIRPersistenceException {

        // common_token_value_id is a generated identity column
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (code_system_id, token_value) ");
        insert.append("     VALUES (?,?) ");

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            for (CommonTokenValue ctv: missing) {
                ps.setInt(1, ctv.getCodeSystemValue().getCodeSystemId());
                ps.setString(2, ctv.getTokenValue());
                try {
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (translator.isDuplicate(x)) {
                        // ignore because this row has already been inserted by another thread
                    } else {
                        throw x;
                    }
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common token values");
        }
    }

    @Override
    protected List<CommonCanonicalValue> fetchCanonicalIds(List<CommonCanonicalValue> unresolved, Map<CommonCanonicalValueKey, CommonCanonicalValue> commonCanonicalValueMap) throws FHIRPersistenceException {
        // For Derby, we get deadlocks when selecting using the in-list method (see parent implementation
        // of this method). Instead, we execute individual statements in the order of the unresolved
        // list so that the (S) locks will be acquired in the same order as the (X) locks obtained when
        // inserting.
        List<CommonCanonicalValue> missing = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT canonical_id ");
        query.append("  FROM common_canonical_values ");
        query.append(" WHERE url = ?");

        final String select = query.toString();
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            for (CommonCanonicalValue ccv: unresolved) {
                ps.setString(1, ccv.getUrl());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ccv.setCanonicalId(rs.getLong(1));
                } else {
                    // entry not found in the database
                    missing.add(ccv);
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "common canonical values fetch failed. SQL=[" + select + "]", x);
            throw new FHIRPersistenceException("common canonical values fetch failed");
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    @Override
    protected void addMissingCommonCanonicalValues(List<CommonCanonicalValue> missing) throws FHIRPersistenceException {

        // for consistency we use fhir_ref_sequence here even though as a bigint, by convention we're supposed
        // to be using fhir_sequence
        final String nextVal = translator.nextValue(schemaName, ParamSchemaConstants.CANONICAL_ID_SEQ);
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (url, canonical_id) ");
        insert.append("     VALUES (?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");

        final String DML = insert.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addMissingCanonicalIds: " + DML);
        }
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            int count = 0;
            for (CommonCanonicalValue ctv: missing) {
                logger.finest(() -> "Adding canonical value [" + ctv.toString() + "]");
                ps.setString(1, ctv.getUrl());
                try {
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (translator.isDuplicate(x)) {
                        // ignore because this row has already been inserted by another thread
                    } else {
                        throw x;
                    }                    
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common canonical values");
        }
    }
}