/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValueResult;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

/**
 * DAO to handle maintenance of the local and external reference tables
 * which contain the relationships described by "reference" elements in
 * each resource (e.g. Observation.subject).
 *
 * The DAO uses a cache for looking up the ids for various entities. The
 * DAO can create new entries, but these can only be used locally until
 * the transaction commits, at which point they can be consolidated into
 * the shared cache. This has the benefit that we reduce the number of times
 * we need to lock the global cache, because we only update it once per
 * transaction.
 *
 * For improved performance, we also make use of batch statements which
 * are managed as member variables. This is why it's important to close
 * this DAO before the transaction commits, ensuring that any outstanding
 * DML batched but not yet executed is processed. Calling close does not
 * close the provided Connection. That is up to the caller to manage.
 * Close does close any statements which are opened inside the class.
 */
public abstract class ResourceReferenceDAO implements IResourceReferenceDAO, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ResourceReferenceDAO.class.getName());

    private final String schemaName;

    // hold on to the connection because we use batches to improve efficiency
    private final Connection connection;

    // The cache used to track the ids of the normalized entities we're managing
    private final ICommonTokenValuesCache cache;

    // The translator for the type of database we are connected to
    private final IDatabaseTranslator translator;

    // The number of operations we allow before submitting a batch
    protected static final int BATCH_SIZE = 100;

    /**
     * Public constructor
     * @param c
     */
    public ResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache) {
        this.translator = t;
        this.connection = c;
        this.cache = cache;
        this.schemaName = schemaName;
    }

    /**
     * Getter for the {@link IDatabaseTranslator} held by this DAO
     * @return
     */
    protected IDatabaseTranslator getTranslator() {
        return this.translator;
    }

    /**
     * Getter for the {@link ICommonTokenValuesCache} held by this DAO
     * @return
     */
    protected ICommonTokenValuesCache getCache() {
        return this.cache;
    }

    /**
     * Getter for the {@link Connection} held by this DAO
     * @return
     */
    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * Getter for subclass access to the schemaName
     * @return
     */
    protected String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public void flush() throws FHIRPersistenceException {
        // NOP at this time
    }

    @Override
    public void close() throws FHIRPersistenceException {
        flush();
    }

    @Override
    public ICommonTokenValuesCache getResourceReferenceCache() {
        return this.cache;
    }

    @Override
    public CommonTokenValueResult readCommonTokenValueId(String codeSystem, String tokenValue) {
        CommonTokenValueResult result;

        final String SQL = ""
                + "SELECT c.code_system_id, c.common_token_value_id "
                + "  FROM common_token_values c,"
                + "       code_systems s "
                + " WHERE c.token_value = ? "
                + "   AND s.code_system_name = ? "
                + "   AND c.code_system_id = s.code_system_id";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, tokenValue);
            ps.setString(2, codeSystem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new CommonTokenValueResult(rs.getInt(1), rs.getLong(2));
            } else {
                result = null;
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SQL, x);
            throw translator.translate(x);
        }

        return result;
    }

    @Override
    public void addCommonTokenValues(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Grab the ids for all the code-systems, and upsert any misses
        List<ResourceTokenValueRec> systemMisses = new ArrayList<>();
        cache.resolveCodeSystems(xrefs, systemMisses);
        upsertCodeSystems(systemMisses);

        // Now that all the code-systems ids are known, we can search the cache
        // for all the token values, upserting anything new
        List<ResourceTokenValueRec> valueMisses = new ArrayList<>();
        cache.resolveTokenValues(xrefs, valueMisses);
        upsertCommonTokenValues(valueMisses);

        insertResourceTokenRefs(resourceType, xrefs);
    }

    /**
     * Insert the values in the resource-type-specific _resource_token_refs table. This
     * is a simple batch insert because all the FKs have already been resolved and updated
     * in the ResourceTokenValueRec records
     * @param resourceType
     * @param xrefs
     */
    protected void insertResourceTokenRefs(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_RESOURCE_TOKEN_REFS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "parameter_name_id, logical_resource_id, common_token_value_id, ref_version_id, composite_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setInt(1, xr.getParameterNameId());
                ps.setLong(2, xr.getLogicalResourceId());

                // common token value can be null
                if (xr.getCommonTokenValueId() != null) {
                    ps.setLong(3, xr.getCommonTokenValueId());
                } else {
                    ps.setNull(3, Types.BIGINT);
                }

                // version can be null
                if (xr.getRefVersionId() != null) {
                    ps.setInt(4, xr.getRefVersionId());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }

                // compositeId can be null
                if (xr.getCompositeId() != null) {
                    ps.setInt(5, xr.getCompositeId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.addBatch();
                if (++count == BATCH_SIZE) {
                    ps.executeBatch();
                    count = 0;
                }
            }

            if (count > 0) {
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert, x);
            throw translator.translate(x);
        }
    }

    /**
     * Add all the systems we currently don't have in the database. If all target
     * databases handled MERGE properly this would be easy, but they don't so
     * we go old-school with a negative outer join instead (which is pretty much
     * what MERGE does behind the scenes anyway).
     * @param systems
     */
    public void upsertCodeSystems(List<ResourceTokenValueRec> systems) {
        if (systems.isEmpty()) {
            return;
        }

        // Unique list so we don't try and create the same name more than once
        Set<String> systemNames = systems.stream().map(xr -> xr.getCodeSystemValue()).collect(Collectors.toSet());
        StringBuilder paramList = new StringBuilder();
        StringBuilder inList = new StringBuilder();
        for (int i=0; i<systemNames.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
                inList.append(",");
            }
            paramList.append("(CAST(? AS VARCHAR(" + FhirSchemaConstants.MAX_SEARCH_STRING_BYTES + ")))");
            inList.append("?");
        }

        final String paramListStr = paramList.toString();
        doCodeSystemsUpsert(paramListStr, systemNames);


        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        StringBuilder select = new StringBuilder();
        select.append("SELECT code_system_name, code_system_id FROM code_systems WHERE code_system_name IN (");
        select.append(inList);
        select.append(")");

        Map<String, Integer> idMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            // load a map with all the ids we need which we can then use to update the
            // ExternalResourceReferenceRec objects
            int a = 1;
            for (String name: systemNames) {
                ps.setString(a++, name);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idMap.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select.toString(), x);
            throw translator.translate(x);
        }

        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: systems) {
            Integer id = idMap.get(xr.getCodeSystemValue());
            if (id != null) {
                xr.setCodeSystemValueId(id);

                // Add this value to the (thread-local) cache
                cache.addCodeSystem(xr.getCodeSystemValue(), id);
            } else {
                // Unlikely...but need to handle just in case
                logger.severe("Record for code_system_name '" + xr.getCodeSystemValue() + "' inserted but not found");
                throw new IllegalStateException("id deleted from database!");
            }
        }
    }

    /**
     * Insert any missing values into the code_systems table
     * @param paramList
     * @param systems
     */
    public abstract void doCodeSystemsUpsert(String paramList, Collection<String> systemNames);

    /**
     * Add reference value records for each unique reference name in the given list
     * @param values
     */
    public void upsertCommonTokenValues(List<ResourceTokenValueRec> values) {

        // Unique list so we don't try and create the same name more than once.
        // Ignore any null token-values, because we don't want to (can't) store
        // them in our common token values table.
        Set<CommonTokenValue> tokenValues = values.stream().filter(x -> x.getTokenValue() != null).map(xr -> new CommonTokenValue(xr.getCodeSystemValueId(), xr.getTokenValue())).collect(Collectors.toSet());

        if (tokenValues.isEmpty()) {
            // nothing to do
            return;
        }

        // Build a string of parameter values we use in the query to drive the insert statement.
        // The database needs to know the type when it parses the query, hence the slightly verbose CAST functions:
        // VALUES ((CAST(? AS VARCHAR(1234)), CAST(? AS INT)), (...)) AS V(common_token_value, parameter_name_id, code_system_id)
        StringBuilder inList = new StringBuilder(); // for the select query later
        StringBuilder paramList = new StringBuilder();
        for (int i=0; i<tokenValues.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
            }
            paramList.append("(CAST(? AS VARCHAR(" + FhirSchemaConstants.MAX_TOKEN_VALUE_BYTES + "))");
            paramList.append(",CAST(? AS INT))");

            // also build the inList for the select statement later
            if (inList.length() > 0) {
                inList.append(",");
            }
            inList.append("(?,?)");
        }

        // query is a negative outer join so we only pick the rows from v for which
        // there is no row found in ctv.
        final String paramListStr = paramList.toString();
        doCommonTokenValuesUpsert(paramListStr, tokenValues);

        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        // Derby doesn't support IN LISTS with multiple members, so we have to join against
        // a VALUES again. No big deal...probably similar amount of work for the database
        StringBuilder select = new StringBuilder();
        select.append("     SELECT ctv.code_system_id, ctv.token_value, ctv.common_token_value_id FROM ");
        select.append("     (VALUES ").append(paramListStr).append(" ) AS v(token_value, code_system_id) ");
        select.append("       JOIN common_token_values ctv ");
        select.append("              ON ctv.token_value = v.token_value ");
        select.append("             AND ctv.code_system_id = v.code_system_id ");

        // Grab the ids
        Map<CommonTokenValue, Long> idMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            int a = 1;
            for (CommonTokenValue tv: tokenValues) {
                ps.setString(a++, tv.getTokenValue());
                ps.setInt(a++, tv.getCodeSystemId());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // code_system_id, token_value
                CommonTokenValue key = new CommonTokenValue(rs.getInt(1), rs.getString(2));
                idMap.put(key, rs.getLong(3));
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: values) {
            // ignore entries with null tokenValue elements - we don't store them in common_token_values
            if (xr.getTokenValue() != null) {
                CommonTokenValue key = new CommonTokenValue(xr.getCodeSystemValueId(), xr.getTokenValue());
                Long id = idMap.get(key);
                if (id != null) {
                    xr.setCommonTokenValueId(id);

                    // update the thread-local cache with this id. The values aren't committed to the shared cache
                    // until the transaction commits
                    cache.addTokenValue(key, id);
                }
            }
        }
    }

    /**
     * Execute the insert (upsert) into the common_token_values table for the
     * given collection of values. Note, this insert from negative outer join
     * requires the database concurrency implementation to be correct. This does
     * not work for Postgres, hence Postgres gets its own implementation of this
     * method
     * @param paramList
     * @param tokenValues
     */
    protected abstract void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> tokenValues);

    @Override
    public void persist(Collection<ResourceTokenValueRec> records) {
        // Grab the ids for all the code-systems, and upsert any misses
        List<ResourceTokenValueRec> systemMisses = new ArrayList<>();
        cache.resolveCodeSystems(records, systemMisses);
        upsertCodeSystems(systemMisses);

        // Now that all the code-systems ids are known, we can search the cache
        // for all the token values, upserting anything new
        List<ResourceTokenValueRec> valueMisses = new ArrayList<>();
        cache.resolveTokenValues(records, valueMisses);
        upsertCommonTokenValues(valueMisses);

        // Now split the records into groups based on resource type.
        Map<String,List<ResourceTokenValueRec>> recordMap = new HashMap<>();
        for (ResourceTokenValueRec rtv: records) {
            List<ResourceTokenValueRec> list = recordMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        for (Map.Entry<String, List<ResourceTokenValueRec>> entry: recordMap.entrySet()) {
            insertResourceTokenRefs(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public List<Long> readCommonTokenValueIdList(String tokenValue) {
        List<Long> result = new ArrayList<>();
        final String SQL = ""
                + "SELECT c.common_token_value_id "
                + "  FROM common_token_values c "
                + " WHERE c.token_value = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, tokenValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getLong(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SQL, x);
            throw translator.translate(x);
        }

        return result;
    }
}