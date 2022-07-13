/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.jdbc.dao.api.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValueResult;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.params.api.ParamSchemaConstants;
import com.ibm.fhir.persistence.params.api.ParameterNameDAO;
import com.ibm.fhir.persistence.params.database.DerbyParameterNamesDAO;


/**
 * Derby-specific extension of the {@link ResourceReferenceDAO} to work around
 * some SQL syntax and Derby concurrency issues
 */
public class DerbyResourceReferenceDAO extends ResourceReferenceDAO {
    private static final Logger logger = Logger.getLogger(DerbyResourceReferenceDAO.class.getName());

    private static final int BATCH_SIZE = 100;

    /**
     * Public constructor
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     * @param parameterNameCache
     * @param logicalResourceIdentCache
     */
    public DerbyResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache, INameIdCache<Integer> parameterNameCache,
            ILogicalResourceIdentCache logicalResourceIdentCache) {
        super(t, c, schemaName, cache, parameterNameCache, logicalResourceIdentCache);
    }

    @Override
    public Set<CommonTokenValueResult> readCommonTokenValueIds(Collection<CommonTokenValue> tokenValues) {
        if (tokenValues.isEmpty()) {
            return Collections.emptySet();
        }

        Set<CommonTokenValueResult> result = new HashSet<>();

        StringBuilder select = new StringBuilder()
                .append("SELECT c.token_value, c.code_system_id, c.common_token_value_id ")
                .append("  FROM common_token_values c")
                .append(" WHERE ");

        String delim = "";
        for (CommonTokenValue ctv : tokenValues) {
            select.append(delim);
            select.append("(c.token_value = ? AND c.code_system_id = " + ctv.getCodeSystemId() + ")");
            delim = " OR ";
        }

        try (PreparedStatement ps = getConnection().prepareStatement(select.toString())) {
            Iterator<CommonTokenValue> iterator = tokenValues.iterator();
            for (int i = 1; i <= tokenValues.size(); i++) {
                ps.setString(i, iterator.next().getTokenValue());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new CommonTokenValueResult(rs.getString(1), rs.getInt(2), rs.getLong(3)));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select.toString(), x);
            throw getTranslator().translate(x);
        }

        return result;
    }

    @Override
    public void doCodeSystemsUpsert(String paramList, Collection<String> sortedSystemNames) {

        // Ideally we'd use an INSERT-FROM-NEGATIVE-OUTER-JOIN here to make sure
        // we only try to insert rows that don't already exist, but this doesn't
        // work with Derby (PostgreSQL has a similar issue, hence the ON CONFLICT
        // DO NOTHING strategy there). For Derby, we are left to handle this
        // ourselves, and just do things row-by-row:
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        final String INS = ""
                + "INSERT INTO code_systems (code_system_id, code_system_name) "
                + "     VALUES (" + nextVal + ", ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(INS)) {
            for (String codeSystemName: sortedSystemNames) {
                ps.setString(1, codeSystemName);
                
                try {
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (getTranslator().isDuplicate(x)) {
                        // ignore because this row has already been inserted by another thread
                    } else {
                        throw x;
                    }
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS, x);
            throw getTranslator().translate(x);
        }
    }
    
    @Override
    protected void doCodeSystemsFetch(Map<String, Integer> idMap, String inList, List<String> sortedSystemNames) {
        // For Derby, we get deadlocks when selecting using the in-list method (see parent implementation
        // of this method). Instead, we execute individual statements in the order of the sortedSystemNames
        // list so that the (S) locks will be acquired in the same order as the (X) locks obtained when
        // inserting.
        final String SQL = "SELECT code_system_id FROM code_systems WHERE code_system_name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(SQL)) {
            for (String codeSystemName: sortedSystemNames) {
                ps.setString(1, codeSystemName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idMap.put(codeSystemName, rs.getInt(1));
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SQL, x);
            throw getTranslator().translate(x);
        }        
    }

    @Override
    public void doCanonicalValuesUpsert(String paramList, Collection<String> sortedURLS) {

        // Derby doesn't like really huge VALUES lists, so we instead need
        // to go with a declared temporary table. As with code_systems_tmp, we generate
        // the id here to allow for better deadlock protection later
        final String nextVal = getTranslator().nextValue(getSchemaName(), ParamSchemaConstants.CANONICAL_ID_SEQ);
        final String insert = "INSERT INTO SESSION.canonical_values_tmp (url, canonical_id) VALUES (?," + nextVal + ")";
        int batchCount = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
            for (String url: sortedURLS) {
                ps.setString(1, url);
                ps.addBatch();

                if (++batchCount == BATCH_SIZE) {
                    ps.executeBatch();
                    batchCount = 0;
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }

        // Upsert values. See the similar code_systems insert for details
        // about deadlock protection
        StringBuilder upsert = new StringBuilder();
        upsert.append("INSERT INTO common_canonical_values (canonical_id, url) ");
        upsert.append("          SELECT src.canonical_id, src.url ");
        upsert.append("            FROM SESSION.canonical_values_tmp src ");
        upsert.append(" LEFT OUTER JOIN common_canonical_values cs ");
        upsert.append("              ON cs.url = src.url ");
        upsert.append("           WHERE cs.url IS NULL ");
        upsert.append("        ORDER BY src.url");

        try (Statement s = getConnection().createStatement()) {
            s.executeUpdate(upsert.toString());
        } catch (SQLException x) {
            logger.log(Level.SEVERE, upsert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> sortedTokenValues) {

        // Doing a sorted INSERT-FROM-NEGATIVE-OUTER-JOIN apparently isn't good enough
        // to avoid deadlock issues in Derby. To address this, we need to go row by
        // row in the sorted order (similar to how CODE_SYSTEMS is handled). In most
        // cases the sortedTokenValues list should only contain new rows. However in
        // high concurrency situations we can still end up with duplicates, which is
        // why we need to handle that here
        final String INS = "INSERT INTO common_token_values (token_value, code_system_id) VALUES (?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(INS)) {
            for (CommonTokenValue ctv: sortedTokenValues) {
                try {
                    ps.setString(1, ctv.getTokenValue());
                    ps.setInt(2, ctv.getCodeSystemId());
                    ps.executeUpdate();
                } catch (SQLException x) {
                    if (getTranslator().isDuplicate(x)) {
                        // do nothing
                    } else {
                        throw x;
                    }
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INS, x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    public void upsertCommonTokenValues(List<ResourceTokenValueRec> values) {
        // Special case for Derby so we don't try and create monster SQL statements
        // resulting in a stack overflow when Derby attempts to parse it.

        // Unique list so we don't try and create the same name more than once.
        // Ignore any null token-values, because we don't want to (can't) store
        // them in our common token values table.
        Set<CommonTokenValue> tokenValueSet = values.stream().filter(x -> x.getTokenValue() != null).map(xr -> new CommonTokenValue(xr.getCodeSystemValue(), xr.getCodeSystemValueId(), xr.getTokenValue())).collect(Collectors.toSet());

        if (tokenValueSet.isEmpty()) {
            // nothing to do
            return;
        }

        // Sort the values so we always process in the same order (deadlock protection)
        List<CommonTokenValue> sortedTokenValues = new ArrayList<>(tokenValueSet);
        sortedTokenValues.sort(CommonTokenValue::compareTo);
        
        final String paramListStr = null;
        doCommonTokenValuesUpsert(paramListStr, sortedTokenValues);

        // Fetch the ids for all the records we need. Because we can have
        // read (S) locks conflicting with write (X) locks, it's important
        // to do this fetching in exactly the same order we try to insert.
        // Unfortunately, for Derby this means going row-by-row (just like we
        // do for CODE_SYSTEMS).
        final String FETCH = ""
                + "  SELECT common_token_value_id "
                + "    FROM common_token_values "
                + "   WHERE token_value = ?"
                + "     AND code_system_id = ?";
        
        Map<CommonTokenValue, Long> idMap = new HashMap<>();
        try (PreparedStatement ps = getConnection().prepareStatement(FETCH)) {
            for (CommonTokenValue ctv: sortedTokenValues) {
                ps.setString(1, ctv.getTokenValue());
                ps.setInt(2, ctv.getCodeSystemId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idMap.put(ctv, rs.getLong(1));
                }
            }
        } catch (SQLException x) {
            throw getTranslator().translate(x);
        }

        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: values) {
            // ignore entries with null tokenValue elements - we don't store them in common_token_values
            if (xr.getTokenValue() != null) {
                CommonTokenValue key = new CommonTokenValue(xr.getCodeSystemValue(), xr.getCodeSystemValueId(), xr.getTokenValue());
                Long id = idMap.get(key);
                if (id != null) {
                    xr.setCommonTokenValueId(id);

                    // update the thread-local cache with this id. The values aren't committed to the shared cache
                    // until the transaction commits
                    getCache().addTokenValue(key, id);
                }
            }
        }
    }
    
    @Override
    protected int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final ParameterNameDAO pnd = new DerbyParameterNamesDAO(getConnection(), getSchemaName());
        return pnd.readOrAddParameterNameId(parameterName);
    }
    
    @Override
    protected PreparedStatement buildLogicalResourceIdentSelectStatement(List<LogicalResourceIdentValue> values) throws SQLException {
        // Derby doesn't support a VALUES table list, so instead we simply build a big
        // OR predicate
        StringBuilder query = new StringBuilder();
        query.append("SELECT lri.resource_type_id, lri.logical_id, lri.logical_resource_id ");
        query.append("  FROM logical_resource_ident AS lri ");
        query.append(" WHERE ");
        for (int i=0; i<values.size(); i++) {
            if (i > 0) {
                query.append(" OR ");
            }
            query.append("(resource_type_id = ? AND logical_id = ?)");
        }
        PreparedStatement ps = getConnection().prepareStatement(query.toString());
        // bind the parameter values
        int param = 1;
        for (LogicalResourceIdentValue val: values) {
            ps.setInt(param++, val.getResourceTypeId());
            ps.setString(param++, val.getLogicalId());
        }

        if (logger.isLoggable(Level.FINE)) {
            String params = String.join(",", values.stream().map(v -> "(" + v.getResourceTypeId() + "," + v.getLogicalId() + ")").collect(Collectors.toList()));
            logger.fine("ident fetch: " + query.toString() + "; params: " + params);
        }

        return ps;
    }

    @Override
    protected void fetchLogicalResourceIdentIds(Map<LogicalResourceIdentKey, LogicalResourceIdentValue> lrIdentMap, List<LogicalResourceIdentValue> unresolved) throws FHIRPersistenceException {
        // For Derby, we opt to do this row by row so that we can keep the selects in order which
        // helps us to avoid deadlocks due to lock compatibility issues with Derby
        StringBuilder query = new StringBuilder();
        query.append("SELECT lri.logical_resource_id ");
        query.append("  FROM logical_resource_ident AS lri ");
        query.append(" WHERE lri.resource_type_id = ? AND lri.logical_id = ?");
        final String sql = query.toString();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            for (LogicalResourceIdentValue value: unresolved) {
                ps.setInt(1, value.getResourceTypeId());
                ps.setString(2, value.getLogicalId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    final long logicalResourceId = rs.getLong(1);
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(value.getResourceTypeId(), value.getLogicalId());
                    value.setLogicalResourceId(logicalResourceId);
                    lrIdentMap.put(key, value);
                } else {
                    // something wrong with our data handling code because we should already have values
                    // for every logical resource at this point
                    throw new FHIRPersistenceException("logical_resource_ident record missing: resourceTypeId["
                            + value.getResourceTypeId() + "] logicalId[" + value.getLogicalId() + "]");
                }
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "logical resource ident fetch failed", x);
            throw new FHIRPersistenceException("logical resource ident fetch failed");
        }
    }
}