/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to add a new tenant key record
 */
public class AddTenantKeyDAO implements IDatabaseStatement {
    private final String schemaName;
    private final int tenantId;
    private final String tenantKey;
    private final String tenantSalt;
    private final String idSequenceName;

    /**
     * Public constructor
     * 
     * @param schemaName
     * @param tenantId
     * @param tenantKey
     * @param tenantSalt
     * @param idSequenceName
     */
    public AddTenantKeyDAO(String schemaName, int tenantId, String tenantKey, String tenantSalt,
            String idSequenceName) {
        DataDefinitionUtil.assertValidName(schemaName);
        this.schemaName = schemaName;
        this.tenantId = tenantId;
        this.tenantKey = tenantKey;
        this.tenantSalt = tenantSalt;
        this.idSequenceName = idSequenceName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        /*
         * Execute the encapsulated query against the database and stream the result
         * data to the configured target
         */
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "TENANT_KEYS");
        final String idSeq = DataDefinitionUtil.getQualifiedName(schemaName, idSequenceName);
        final String SQL = "" + "   INSERT INTO " + tableName + "(tenant_key_id, mt_id, tenant_salt, tenant_hash)"
                + "        VALUES (next value for " + idSeq + ", ?, ?, SYSIBM.HASH(? || ?, 2))";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setInt(1, tenantId);
            ps.setString(2, tenantSalt);
            ps.setString(3, tenantSalt); // | Database only stores the hash of these
            ps.setString(4, tenantKey); // | two values combined. The key is not stored
            ps.executeUpdate();
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}
