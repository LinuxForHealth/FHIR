/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterNameDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;


/**
 * Db2-specific extension of the {@link ResourceReferenceDAO} to work around
 * some SQL syntax and Postgres concurrency issues
 */
public class Db2ResourceReferenceDAO extends ResourceReferenceDAO {
    private static final Logger logger = Logger.getLogger(Db2ResourceReferenceDAO.class.getName());

    // The name of the admin schema containing the SV_TENANT_ID variable
    private final String adminSchemaName;

    /**
     * Public constructor
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     */
    public Db2ResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache, String adminSchemaName, INameIdCache<Integer> parameterNameCache) {
        super(t, c, schemaName, cache, parameterNameCache);
        this.adminSchemaName = adminSchemaName;
    }

    @Override
    public void doCodeSystemsUpsert(String paramList, Collection<String> sortedSystemNames) {
        // query is a negative outer join so we only pick the rows where
        // the row "s" from the actual table doesn't exist.
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (mt_id, code_system_id, code_system_name) ");
        insert.append("     SELECT ").append(adminSchemaName).append(".sv_tenant_id, ");
        insert.append(nextVal).append(", v.name ");
        insert.append("            FROM (VALUES ").append(paramList).append(" ) AS v(name) ");
        insert.append(" LEFT OUTER JOIN code_systems s ");
        insert.append("              ON s.code_system_name = v.name ");
        insert.append("           WHERE s.code_system_name IS NULL ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the code_system_name values as parameters
            int a = 1;
            for (String name: sortedSystemNames) {
                ps.setString(a++, name);
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    public void doCanonicalValuesUpsert(String paramList, Collection<String> sortedURLS) {
        // query is a negative outer join so we only pick the rows where
        // the row "s" from the actual table doesn't exist.
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (mt_id, canonical_id, url) ");
        insert.append("     SELECT ").append(adminSchemaName).append(".sv_tenant_id, ");
        insert.append(nextVal).append(", v.name ");
        insert.append("            FROM (VALUES ").append(paramList).append(" ) AS v(name) ");
        insert.append(" LEFT OUTER JOIN common_canonical_values s ");
        insert.append("              ON s.url = v.name ");
        insert.append("           WHERE s.url IS NULL ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the code_system_name values as parameters
            int a = 1;
            for (String name: sortedURLS) {
                ps.setString(a++, name);
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> sortedTokenValues) {
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (mt_id, token_value, code_system_id) ");
        insert.append("     SELECT ").append(adminSchemaName).append(".sv_tenant_id");
        insert.append(", v.token_value, v.code_system_id FROM ");
        insert.append("     (VALUES ").append(paramList).append(" ) AS v(token_value, code_system_id) ");
        insert.append(" LEFT OUTER JOIN common_token_values ctv ");
        insert.append("              ON ctv.token_value = v.token_value ");
        insert.append("             AND ctv.code_system_id = v.code_system_id ");
        insert.append("      WHERE ctv.token_value IS NULL ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the name values as parameters
            int a = 1;
            for (CommonTokenValue tv: sortedTokenValues) {
                ps.setString(a++, tv.getTokenValue());
                ps.setInt(a++, tv.getCodeSystemId());
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.throwing(Db2ResourceReferenceDAO.class.getSimpleName(), "doCommonTokenValuesUpsert", x);
            StringBuilder values = new StringBuilder();
            for (CommonTokenValue tv: sortedTokenValues) {
                if (values.length() > 0) {
                    values.append(", ");
                }
                values.append("{");
                values.append(tv.getTokenValue());
                values.append(",");
                values.append(tv.getCodeSystemId());
                values.append("}");
            }

            logger.log(Level.SEVERE, "Unable to upsert to common_token_values [" + insert.toString() + ";] [" + values.toString() + "]", x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertResourceTokenRefs(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch. This is the multitenant variant used for Db2
        final String tableName = resourceType + "_RESOURCE_TOKEN_REFS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, parameter_name_id, logical_resource_id, common_token_value_id, ref_version_id, composite_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertSystemResourceTokenRefs(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = "RESOURCE_TOKEN_REFS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, parameter_name_id, logical_resource_id, common_token_value_id, ref_version_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertResourceProfiles(String resourceType, Collection<ResourceProfileRec> profiles) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_PROFILES";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, canonical_id, version, fragment) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertSystemResourceProfiles(String resourceType, Collection<ResourceProfileRec> profiles) {
        final String tableName = "LOGICAL_RESOURCE_PROFILES";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, canonical_id, version, fragment) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertResourceTags(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_TAGS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, common_token_value_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertSystemResourceTags(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName =  "LOGICAL_RESOURCE_TAGS";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, common_token_value_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }
    @Override
    protected void insertResourceSecurity(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName = resourceType + "_SECURITY";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, common_token_value_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void insertSystemResourceSecurity(String resourceType, Collection<ResourceTokenValueRec> xrefs) {
        // Now all the values should have ids assigned so we can go ahead and insert them
        // as a batch
        final String tableName =  "LOGICAL_RESOURCE_SECURITY";
        DataDefinitionUtil.assertValidName(tableName);
        final String insert = "INSERT INTO " + tableName + "("
                + "mt_id, logical_resource_id, common_token_value_id) "
                + "VALUES (" + this.adminSchemaName + ".SV_TENANT_ID, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
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
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final ParameterNameDAO pnd = new ParameterNameDAOImpl(getConnection(), getSchemaName());
        return pnd.readOrAddParameterNameId(parameterName);
    }
}