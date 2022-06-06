/*
 * (C) Copyright IBM Corp. 2020, 2022
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.jdbc.dao.api.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValueResult;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
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

    // Cache of parameter names to id
    private final INameIdCache<Integer> parameterNameCache;

    // Cache of the logical resource id values from logical_resource_ident
    private final ILogicalResourceIdentCache logicalResourceIdentCache;

    // The translator for the type of database we are connected to
    private final IDatabaseTranslator translator;

    // The number of operations we allow before submitting a batch
    protected static final int BATCH_SIZE = 100;

    /**
     * Public constructor
     * 
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     * @param parameterNameCache
     * @param logicalResourceIdentCache
     */
    public ResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache, INameIdCache<Integer> parameterNameCache, ILogicalResourceIdentCache logicalResourceIdentCache) {
        this.translator = t;
        this.schemaName = schemaName;
        this.connection = c;
        this.cache = cache;
        this.parameterNameCache = parameterNameCache;
        this.logicalResourceIdentCache = logicalResourceIdentCache;
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
                result = new CommonTokenValueResult(null, rs.getInt(1), rs.getLong(2));
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
    public Set<CommonTokenValueResult> readCommonTokenValueIds(Collection<CommonTokenValue> tokenValues) {
        if (tokenValues.isEmpty()) {
            return Collections.emptySet();
        }

        Set<CommonTokenValueResult> result = new HashSet<>();

        StringBuilder select = new StringBuilder()
                .append("SELECT c.token_value, c.code_system_id, c.common_token_value_id ")
                .append("  FROM common_token_values c")
                .append("  JOIN (VALUES ");

        String delim = "";
        for (CommonTokenValue tokenValue : tokenValues) {
            select.append(delim);
            // CodeSystemId is an int that comes from the db so we can use the literal for that.
            // TokenValue is a string that could possibly come from the end user, so that should be a bind variable.
            select.append("(?," + tokenValue.getCodeSystemId() + ")");
            delim = ", ";
        }

        select.append(") AS tmp (token_value,code_system_id) ON tmp.token_value = c.token_value AND tmp.code_system_id = c.code_system_id");

        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            Iterator<CommonTokenValue> iterator = tokenValues.iterator();
            for (int i = 1; i <= tokenValues.size(); i++) {
                CommonTokenValue tokenValue = iterator.next();

                ps.setString(i, tokenValue.getTokenValue());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new CommonTokenValueResult(rs.getString(1), rs.getInt(2), rs.getLong(3)));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, select.toString(), x);
            throw translator.translate(x);
        }

        return result;
    }

    @Override
    public Integer readCanonicalId(String canonicalValue) {
        Integer result;
        final String SQL = ""
                + "SELECT canonical_id "
                + "  FROM common_canonical_values "
                + " WHERE url = ? ";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, canonicalValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
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
    public void addNormalizedValues(String resourceType, Collection<ResourceTokenValueRec> xrefs, Collection<ResourceReferenceValueRec> resourceRefs, Collection<ResourceProfileRec> profileRecs, Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException {
        // This method is only called when we're not using transaction data
        logger.fine("Persist parameters for this resource - no transaction data available");
        persist(xrefs, resourceRefs, profileRecs, tagRecs, securityRecs);
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
     * Insert the values in the resource-type-specific _ref_values table. This
     * is a simple batch insert because all the FKs have already been resolved and updated
     * in the ResourceTokenValueRec records
     * @param resourceType
     * @param xrefs
     */
    protected void insertRefValues(String resourceType, Collection<ResourceReferenceValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_REF_VALUES";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "parameter_name_id, logical_resource_id, ref_logical_resource_id, ref_version_id, composite_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            PreparedStatementHelper psh = new PreparedStatementHelper(ps);
            for (ResourceReferenceValueRec xr: xrefs) {
                psh.setInt(xr.getParameterNameId())
                    .setLong(xr.getLogicalResourceId())
                    .setLong(xr.getRefLogicalResourceId())
                    .setInt(xr.getRefVersionId())
                    .setInt(xr.getCompositeId())
                    .addBatch();

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
     * Insert any whole-system parameters to the token_refs table
     * @param resourceType
     * @param xrefs
     */
    protected void insertSystemResourceTokenRefs(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = "RESOURCE_TOKEN_REFS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "parameter_name_id, logical_resource_id, common_token_value_id, ref_version_id) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                if (xr.isSystemLevel()) {
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

                    ps.addBatch();
                    if (++count == BATCH_SIZE) {
                        ps.executeBatch();
                        count = 0;
                    }
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
        final Set<String> systemNames = systems.stream().map(xr -> xr.getCodeSystemValue()).collect(Collectors.toSet());
        final List<String> sortedSystemNames = new ArrayList<>(systemNames);
        sortedSystemNames.sort(String::compareTo);
        final StringBuilder paramList = new StringBuilder();
        final StringBuilder inList = new StringBuilder();
        for (int i=0; i<sortedSystemNames.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
                inList.append(",");
            }
            paramList.append("(CAST(? AS VARCHAR(" + FhirSchemaConstants.MAX_SEARCH_STRING_BYTES + ")))");
            inList.append("?");
        }

        final String paramListStr = paramList.toString();
        doCodeSystemsUpsert(paramListStr, sortedSystemNames);

        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        final Map<String, Integer> idMap = new HashMap<>();
        doCodeSystemsFetch(idMap, inList.toString(), sortedSystemNames);

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
     * Fetch the code_system_id values for each of the code_system_name values in the sortedSystemNames list.
     * @param idMap the code_system_name -> code_system_id map to populate
     * @param inList a list of bind markers for the values in the sortedSystemNames list
     * @param sortedSystemNames the list of code_system_name values to fetch
     */
    protected void doCodeSystemsFetch(Map<String, Integer> idMap, String inList, List<String> sortedSystemNames) {
        StringBuilder select = new StringBuilder();
        select.append("SELECT code_system_name, code_system_id FROM code_systems WHERE code_system_name IN (");
        select.append(inList);
        select.append(")");

        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            // load a map with all the ids we need which we can then use to update the
            // ExternalResourceReferenceRec objects
            int a = 1;
            for (String name: sortedSystemNames) {
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
    }

    /**
     * Add the missing values to the database (and get ids allocated)
     * @param profileValues
     */
    public void upsertCanonicalValues(List<ResourceProfileRec> profileValues) {
        if (profileValues.isEmpty()) {
            return;
        }

        // Unique list so we don't try and create the same name more than once
        Set<String> valueSet = profileValues.stream().map(xr -> xr.getCanonicalValue()).collect(Collectors.toSet());
        List<String> sortedValues = new ArrayList<String>(valueSet);
        sortedValues.sort(String::compareTo);

        StringBuilder paramList = new StringBuilder();
        StringBuilder inList = new StringBuilder();
        for (int i=0; i<sortedValues.size(); i++) {
            if (paramList.length() > 0) {
                paramList.append(", ");
                inList.append(",");
            }
            paramList.append("(CAST(? AS VARCHAR(" + FhirSchemaConstants.CANONICAL_URL_BYTES + ")))");
            inList.append("?");
        }

        final String paramListStr = paramList.toString();
        doCanonicalValuesUpsert(paramListStr, sortedValues);


        // Now grab the ids for the rows we just created. If we had a RETURNING implementation
        // which worked reliably across all our database platforms, we wouldn't need this
        // second query.
        StringBuilder select = new StringBuilder();
        select.append("SELECT url, canonical_id FROM common_canonical_values WHERE url IN (");
        select.append(inList);
        select.append(")");

        Map<String, Integer> idMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(select.toString())) {
            // load a map with all the ids we need which we can then use to update the
            // ExternalResourceReferenceRec objects
            int a = 1;
            for (String name: sortedValues) {
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
        for (ResourceProfileRec xr: profileValues) {
            Integer id = idMap.get(xr.getCanonicalValue());
            if (id != null) {
                xr.setCanonicalValueId(id);

                // Add this value to the (thread-local) cache
                cache.addCanonicalValue(xr.getCanonicalValue(), id);
            } else {
                // Unlikely...but need to handle just in case
                logger.severe("Record for common_canonical_value '" + xr.getCanonicalValue() + "' inserted but not found");
                throw new IllegalStateException("id deleted from database!");
            }
        }
    }

    protected void insertResourceProfiles(String resourceType, Collection<ResourceProfileRec> profiles) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_PROFILES";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, canonical_id, version, fragment) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceProfileRec xr: profiles) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setInt(2, xr.getCanonicalValueId());

                // canonical version can be null
                if (xr.getVersion() != null) {
                    ps.setString(3, xr.getVersion());
                } else {
                    ps.setNull(3, Types.VARCHAR);
                }

                // canonical fragment can be null
                if (xr.getFragment() != null) {
                    ps.setString(4, xr.getFragment());
                } else {
                    ps.setNull(4, Types.VARCHAR);
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
     * Insert PROFILE parameters
     * @param resourceType
     * @param profiles
     */
    protected void insertSystemResourceProfiles(String resourceType, Collection<ResourceProfileRec> profiles) {
        final String tableName = "LOGICAL_RESOURCE_PROFILES";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, canonical_id, version, fragment) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceProfileRec xr: profiles) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setInt(2, xr.getCanonicalValueId());

                // canonical version can be null
                if (xr.getVersion() != null) {
                    ps.setString(3, xr.getVersion());
                } else {
                    ps.setNull(3, Types.VARCHAR);
                }

                // canonical fragment can be null
                if (xr.getFragment() != null) {
                    ps.setString(4, xr.getFragment());
                } else {
                    ps.setNull(4, Types.VARCHAR);
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
     * Insert the tags referenced by the given collection of token value records
     * @param resourceType
     * @param xrefs
     */
    protected void insertResourceTags(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_TAGS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, common_token_value_id) "
                + "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setLong(2, xr.getCommonTokenValueId());
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
     * Insert _tag parameters to the whole-system LOGICAL_RESOURCE_TAGS table
     * @param resourceType
     * @param xrefs
     */
    protected void insertSystemResourceTags(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName =  "LOGICAL_RESOURCE_TAGS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, common_token_value_id) "
                + "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setLong(2, xr.getCommonTokenValueId());
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
     * Insert _security parameters to the resource-specific xx_SECURITY table
     * @param resourceType
     * @param xrefs
     */
    protected void insertResourceSecurity(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_SECURITY";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, common_token_value_id) "
                + "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setLong(2, xr.getCommonTokenValueId());
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
     * Insert _security parametes to the whole-system LOGICAL_REOURCE_SECURITY table
     * @param resourceType
     * @param xrefs
     */
    protected void insertSystemResourceSecurity(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName =  "LOGICAL_RESOURCE_SECURITY";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "logical_resource_id, common_token_value_id) "
                + "VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            int count = 0;
            for (ResourceTokenValueRec xr: xrefs) {
                ps.setLong(1, xr.getLogicalResourceId());
                ps.setLong(2, xr.getCommonTokenValueId());
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
     * Insert any missing values into the code_systems table
     * @param paramList
     * @param systemNames a sorted collection of system names
     */
    public abstract void doCodeSystemsUpsert(String paramList, Collection<String> sortedSystemNames);

    /**
     * Insert any missing values into the common_canonical_values table
     * @param paramList
     * @param urls
     */
    public abstract void doCanonicalValuesUpsert(String paramList, Collection<String> sortedURLS);

    /**
     * Add reference value records for each unique reference name in the given list
     * @param values
     */
    public void upsertCommonTokenValues(List<ResourceTokenValueRec> values) {

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

        // Process the data in a window.
        int idx = 0;
        int max = sortedTokenValues.size();

        // The maximum number of query parameters that are available for a particular persistence layer.
        // There are two '?' parameters declared for each CommonTokenValue.
        Optional<Integer> maxQuery = translator.maximumQueryParameters();
        int maxSub;
        if (maxQuery.isPresent() && (max * 2) > maxQuery.get()) {
            maxSub = maxQuery.get() / 2;
        } else {
            maxSub = max;
        }

        // The sliding window
        int window = 1;
        while (idx < max) {
            List<CommonTokenValue> sortedTokenValuesSub = new ArrayList<>();
        
            // Build a string of parameter values we use in the query to drive the insert statement.
            // The database needs to know the type when it parses the query, hence the slightly verbose CAST functions:
            // VALUES ((CAST(? AS VARCHAR(1234)), CAST(? AS INT)), (...)) AS V(common_token_value, parameter_name_id, code_system_id)
            StringBuilder inList = new StringBuilder(); // for the select query later
            StringBuilder paramList = new StringBuilder();
            for (; idx < (maxSub * window) && idx < max; idx++) {
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

                sortedTokenValuesSub.add(sortedTokenValues.get(idx));
            }

            // Condition where there are more than one
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("sortedTokenValuesSub=[" + sortedTokenValuesSub.size() + "] sortedTokenValues=[" + sortedTokenValues.size() + "]");
            }

            final String paramListStr = paramList.toString();
            doCommonTokenValuesUpsert(paramListStr, sortedTokenValuesSub);

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
                for (CommonTokenValue tv: sortedTokenValuesSub) {
                    ps.setString(a++, tv.getTokenValue());
                    ps.setInt(a++, tv.getCodeSystemId());
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    // SELECT code_system_id, token_value...note codeSystem not required
                    CommonTokenValue key = new CommonTokenValue(null, rs.getInt(1), rs.getString(2));
                    idMap.put(key, rs.getLong(3));
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }

            // Now update the ids for all the matching systems in our list
            for (ResourceTokenValueRec xr: values) {
                // ignore entries with null tokenValue elements - we don't store them in common_token_values
                if (xr.getTokenValue() != null) {
                    CommonTokenValue key = new CommonTokenValue(null, xr.getCodeSystemValueId(), xr.getTokenValue());
                    Long id = idMap.get(key);
                    if (id != null) {
                        xr.setCommonTokenValueId(id);

                        // update the thread-local cache with this id. The values aren't committed to the shared cache
                        // until the transaction commits
                        cache.addTokenValue(key, id);
                    }
                }
            }
            window++;
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
    protected abstract void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> sortedTokenValues);

    @Override
    public void persist(Collection<ResourceTokenValueRec> records, Collection<ResourceReferenceValueRec> referenceRecords, Collection<ResourceProfileRec> profileRecs, Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException {

        boolean gotSomething = collectAndResolveParameterNames(records, referenceRecords, profileRecs, tagRecs, securityRecs);
        if (!gotSomething) {
            // nothing to do
            return;
        }

        // Grab the ids for all the code-systems, and upsert any misses
        List<ResourceTokenValueRec> systemMisses = new ArrayList<>();
        cache.resolveCodeSystems(records, systemMisses);
        cache.resolveCodeSystems(tagRecs, systemMisses);
        cache.resolveCodeSystems(securityRecs, systemMisses);
        upsertCodeSystems(systemMisses);

        // Now that all the code-systems ids are known, we can search the cache
        // for all the token values, upserting anything new
        List<ResourceTokenValueRec> valueMisses = new ArrayList<>();
        cache.resolveTokenValues(records, valueMisses);
        cache.resolveTokenValues(tagRecs, valueMisses);
        cache.resolveTokenValues(securityRecs, valueMisses);
        upsertCommonTokenValues(valueMisses);

        // Resolve all the LOGICAL_RESOURCE_IDENT records we need as reference targets
        List<ResourceReferenceValueRec> referenceMisses = new ArrayList<>();
        logicalResourceIdentCache.resolveReferenceValues(referenceRecords, referenceMisses);
        upsertLogicalResourceIdents(referenceMisses);

        // Process all the common canonical values
        List<ResourceProfileRec> canonicalMisses = new ArrayList<>();
        cache.resolveCanonicalValues(profileRecs, canonicalMisses);
        upsertCanonicalValues(canonicalMisses);

        // Now split the token-value records into groups based on resource type.
        Map<String,List<ResourceTokenValueRec>> recordMap = new HashMap<>();
        for (ResourceTokenValueRec rtv: records) {
            List<ResourceTokenValueRec> list = recordMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        for (Map.Entry<String, List<ResourceTokenValueRec>> entry: recordMap.entrySet()) {
            insertResourceTokenRefs(entry.getKey(), entry.getValue());
            insertSystemResourceTokenRefs(entry.getKey(), entry.getValue());
        }

        // Split reference records by resource type
        Map<String,List<ResourceReferenceValueRec>> referenceRecordMap = new HashMap<>();
        for (ResourceReferenceValueRec rtv: referenceRecords) {
            List<ResourceReferenceValueRec> list = referenceRecordMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        // process each list of reference values by resource type
        for (Map.Entry<String, List<ResourceReferenceValueRec>> entry: referenceRecordMap.entrySet()) {
            insertRefValues(entry.getKey(), entry.getValue());
        }
        
        // Split profile values by resource type
        Map<String,List<ResourceProfileRec>> profileMap = new HashMap<>();
        for (ResourceProfileRec rtv: profileRecs) {
            List<ResourceProfileRec> list = profileMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        for (Map.Entry<String, List<ResourceProfileRec>> entry: profileMap.entrySet()) {
            insertResourceProfiles(entry.getKey(), entry.getValue());
            insertSystemResourceProfiles(entry.getKey(), entry.getValue());
        }

        // Split tag records by resource type
        Map<String,List<ResourceTokenValueRec>> tagMap = new HashMap<>();
        for (ResourceTokenValueRec rtv: tagRecs) {
            List<ResourceTokenValueRec> list = tagMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        for (Map.Entry<String, List<ResourceTokenValueRec>> entry: tagMap.entrySet()) {
            insertResourceTags(entry.getKey(), entry.getValue());
            insertSystemResourceTags(entry.getKey(), entry.getValue());
        }

        // Split security records by resource type
        Map<String,List<ResourceTokenValueRec>> securityMap = new HashMap<>();
        for (ResourceTokenValueRec rtv: securityRecs) {
            List<ResourceTokenValueRec> list = securityMap.computeIfAbsent(rtv.getResourceType(), k -> { return new ArrayList<>(); });
            list.add(rtv);
        }

        for (Map.Entry<String, List<ResourceTokenValueRec>> entry: securityMap.entrySet()) {
            insertResourceSecurity(entry.getKey(), entry.getValue());
            insertSystemResourceSecurity(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Build a unique list of parameter names then sort before resolving the ids. This reduces
     * the chance we'll get a deadlock under high concurrency conditions
     * @param records
     * @param referenceRecords
     * @param profileRecs
     * @param tagRecs
     * @param securityRecs
     * @return true if we processed one or more records
     */
    private boolean collectAndResolveParameterNames(Collection<ResourceTokenValueRec> records, Collection<ResourceReferenceValueRec> referenceRecords, Collection<ResourceProfileRec> profileRecs,
        Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException {

        List<ResourceRefRec> recList = new ArrayList<>();
        recList.addAll(records);
        recList.addAll(referenceRecords);
        recList.addAll(profileRecs);
        recList.addAll(tagRecs);
        recList.addAll(securityRecs);

        // Build a unique list of parameter names and then sort
        Set<String> parameterNameSet = new HashSet<>(recList.stream().map(rec -> rec.getParameterName()).collect(Collectors.toList()));
        List<String> parameterNameList = new ArrayList<>(parameterNameSet);
        parameterNameList.sort(String::compareTo);

        // Do lookups in order (deadlock protection)
        for (String parameterName: parameterNameList) {
            // The cache holds a local map of name to id, so no need to duplicate that here
            getParameterNameId(parameterName);
        }

        // Fetch the values that we just cached in the previous loop
        for (ResourceRefRec rec: recList) {
            rec.setParameterNameId(getParameterNameId(rec.getParameterName()));
        }
        return recList.size() > 0;
    }

    @Override
    public List<Long> readCommonTokenValueIdList(final String tokenValue) {
        final List<Long> result = new ArrayList<>();
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

    /**
     * Get the id from the local (tenant-specific) identity cache, or read/create using
     * the database if needed.
     * @param parameterName
     * @return
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceDataAccessException
     */
    protected int getParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        Integer result = parameterNameCache.getId(parameterName);
        if (result == null) {
            result = readOrAddParameterNameId(parameterName);
            parameterNameCache.addEntry(parameterName, result);
        }
        return result;
    }

    /**
     * Fetch the id for the given parameter name from the database, creating a new entry if required.
     * @param parameterName
     * @return
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceDataAccessException
     */
    protected abstract int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;

    protected void upsertLogicalResourceIdents(List<ResourceReferenceValueRec> unresolved) throws FHIRPersistenceException {
        if (unresolved.isEmpty()) {
            return;
        }

        // Build a unique set of logical_resource_ident keys
        Set<LogicalResourceIdentValue> keys = unresolved.stream().map(v -> new LogicalResourceIdentValue(v.getRefResourceTypeId(), v.getRefLogicalId())).collect(Collectors.toSet());
        List<LogicalResourceIdentValue> missing = new ArrayList<>(keys);
        // Sort the list in logicalId,resourceTypeId  order
        missing.sort((a,b) -> {
            int result = a.getLogicalId().compareTo(b.getLogicalId());
            if (result == 0) {
                result = Integer.compare(a.getResourceTypeId(), b.getResourceTypeId());
            }
            return result;
        });
        addMissingLogicalResourceIdents(missing);

        // Now fetch all the identity records we just created so that we can 
        // process the unresolved list of ResourceReferenceValueRec records
        Map<LogicalResourceIdentKey,LogicalResourceIdentValue> lrIdentMap = new HashMap<>();
        fetchLogicalResourceIdentIds(lrIdentMap, missing);

        // Now we can use the map to find the logical_resource_id for each of the unresolved
        // ResourceReferenceValueRec records
        for (ResourceReferenceValueRec rec: unresolved) {
            LogicalResourceIdentKey key = new LogicalResourceIdentKey(rec.getRefResourceTypeId(), rec.getRefLogicalId());
            LogicalResourceIdentValue val = lrIdentMap.get(key);
            if (val != null) {
                rec.setRefLogicalResourceId(val.getLogicalResourceId());
            } else {
                // Shouldn't happen, but be defensive in case someone breaks something
                throw new FHIRPersistenceException("logical_resource_idents still missing after upsert");
            }
        }
    }


    /**
     * Build and prepare a statement to fetch the code_system_id and code_system_name
     * from the code_systems table for all the given (unresolved) code system values
     * @param values
     * @return
     * @throws SQLException
     */
    protected PreparedStatement buildLogicalResourceIdentSelectStatement(List<LogicalResourceIdentValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT lri.resource_type_id, lri.logical_id, lri.logical_resource_id ");
        query.append("  FROM logical_resource_ident AS lri ");
        query.append("  JOIN (VALUES ");
        for (int i=0; i<values.size(); i++) {
            if (i > 0) {
                query.append(",");
            }
            query.append("(?,?)");
        }
        query.append(") AS v(resource_type_id, logical_id) ");
        query.append(" ON (lri.resource_type_id = v.resource_type_id AND lri.logical_id = v.logical_id)");
        PreparedStatement ps = connection.prepareStatement(query.toString());
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

    /**
     * These logical_resource_ident values weren't found in the database, so we need to try and add them.
     * We have to deal with concurrency here - there's a chance another thread could also
     * be trying to add them. To avoid deadlocks, it's important to do any inserts in a
     * consistent order. At the end, we should be able to read back values for each entry
     * @param missing
     */
    protected void addMissingLogicalResourceIdents(List<LogicalResourceIdentValue> missing) throws FHIRPersistenceException {

        // simplified implementation which handles inserts individually
        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO logical_resource_ident (resource_type_id, logical_id, logical_resource_id) VALUES (?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(")");

        logger.fine(() -> "ident insert: " + insert.toString());
        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            for (LogicalResourceIdentKey value: missing) {
                ps.setInt(1, value.getResourceTypeId());
                ps.setString(2, value.getLogicalId());
                try {
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
            logger.log(Level.SEVERE, "logical_resource_ident insert failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("logical_resource_ident insert failed");
        }
    }

    protected void fetchLogicalResourceIdentIds(Map<LogicalResourceIdentKey, LogicalResourceIdentValue> lrIdentMap, List<LogicalResourceIdentValue> unresolved) throws FHIRPersistenceException {

        int resultCount = 0;
        final int maxValuesPerStatement = 512;
        int offset = 0;
        while (offset < unresolved.size()) {
            int remaining = unresolved.size() - offset;
            int subSize = Math.min(remaining, maxValuesPerStatement);
            List<LogicalResourceIdentValue> sub = unresolved.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration
            try (PreparedStatement ps = buildLogicalResourceIdentSelectStatement(sub)) {
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each LogicalResourceIdentValue
                while (rs.next()) {
                    resultCount++;
                    final int resourceTypeId = rs.getInt(1);
                    final String logicalId = rs.getString(2);
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(resourceTypeId, logicalId);
                    LogicalResourceIdentValue identValue = new LogicalResourceIdentValue(resourceTypeId, logicalId);
                    identValue.setLogicalResourceId(rs.getLong(3));
                    lrIdentMap.put(key, identValue);
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "logical resource ident fetch failed", x);
                throw new FHIRPersistenceException("logical resource ident fetch failed");
            }
        }
        // quick check to make sure we got everything we expected
        if (resultCount < unresolved.size()) {
            throw new FHIRPersistenceException("logical_resource_ident fetch did not fetch everything expected");
        }
    }
}