/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.version.SchemaConstants;

/**
 * Obtain a lease for this instance of the schema update tool. 
 * Only the tool holding the lease is allowed to modify the
 * schema.
 */
public class GetLease implements IDatabaseSupplier<Boolean> {
    private static final Logger logger = Logger.getLogger(GetLease.class.getName());
    private final String adminSchema;
    
    // The FHIR data schema for which we want to obtain the lease
    private final String schemaName;

    // The hostname so we can debug which instance owns a particular lease
    private final String host;
    
    // An identifier unique to this instance of the schema update tool
    private final String leaseId;
    
    // The UTC time up to which we want to own the lease
    private final Instant leaseUntil;
    
    /**
     * Public constructor
     * 
     * @param adminSchema
     * @param schemaName
     * @param host
     * @param leaseId
     * @param leaseUntil
     */
    public GetLease(String adminSchema, String schemaName, String host, String leaseId, Instant leaseUntil) {
        this.adminSchema = adminSchema;
        this.schemaName = schemaName;
        this.host = host;
        this.leaseId = leaseId;
        this.leaseUntil = leaseUntil;
    }

    /**
     * Get the insert statement
     * 
     * @return
     */
    protected String getInsertSQL(final String adminSchema) {
        final String CONTROL = DataDefinitionUtil.getQualifiedName(adminSchema, SchemaConstants.CONTROL);
        final String result = "INSERT INTO " + CONTROL + " ("
                + " schema_name, lease_owner_host, lease_owner_uuid, lease_until) "
                + " VALUES (?, ?, ?, ?)";
        return result;
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        final String CONTROL = DataDefinitionUtil.getQualifiedName(adminSchema, SchemaConstants.CONTROL);
        Boolean result = Boolean.FALSE;
        
        // Try to obtain the lease. This operation is supposed to be in a
        // short transaction - there's no reliance on long-term locking here.
        // An instance is expected to renew its lease periodically (at half time)
        // If the instance owns the lease but a lease update fails, the instance 
        // is expected to terminate immediately.
        final Calendar utc = CalendarHelper.getCalendarForUTC();
        final String INS = getInsertSQL(this.adminSchema);
        
        boolean locked = false;
        while (!locked) {
            try (PreparedStatement ps = c.prepareStatement(INS)) {
                ps.setString(1, this.schemaName);
                ps.setString(2, host);
                ps.setString(3, leaseId);
                ps.setTimestamp(4, Timestamp.from(leaseUntil), utc);

                // To support PostgreSQL, we need to ON CONFLICT DO NOTHING - if
                // there's a conflict executeUpdate would return 0 instead of
                // an exception
                if (1 == ps.executeUpdate()) {
                    locked = true; // we inserted the row, so we must own the lock
                    result = Boolean.TRUE; // and also own the lease
                }
            } catch (SQLException x) {
                // if the row is a duplicate, we drop through to the SEL statement
                if (!translator.isDuplicate(x)) {
                    throw translator.translate(x);
                }
            }
            
            // Failed because there's a duplicate row, meaning that there's a current
            // lease record which may or may not be current. Try and obtain
            // the lease by updating the row with our details - which will only work
            // if the current lease has expired, or we currently hold the lease in which
            // case we should just be extending the lease. But before we try this update,
            // we need to obtain a SELECT FOR UPDATE lock on the row because it's possible
            // the current lease-holder could delete the row just before we execute the
            // update lease statement
            if (!locked) {
                final String SEL = translator.addForUpdate("SELECT 1 FROM " + CONTROL + " WHERE schema_name = ?");
                try (PreparedStatement ps = c.prepareStatement(SEL)) {
                    ps.setString(1, schemaName);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        locked = true;
                    }
                } catch (SQLException x) {
                    throw translator.translate(x);
                }
            
                if (locked) {
                    // Got the lock so we know the row exists and no-one can delete it until we're done
                    final String UPD = "UPDATE " + CONTROL
                            + "  SET lease_owner_host = ?, "
                            + "      lease_owner_uuid = ?, "
                            + "      lease_until = ? "
                            + "WHERE schema_name = ? "
                            + "  AND (lease_until < ? "      // expired
                            + "   OR lease_owner_uuid = ?)"; // we already own it

                    try (PreparedStatement ps = c.prepareStatement(UPD)) {
                        ps.setString(1, host);
                        ps.setString(2, leaseId);
                        ps.setTimestamp(3, Timestamp.from(leaseUntil), utc);
                        ps.setString(4, this.schemaName);
                        ps.setTimestamp(5, Timestamp.from(Instant.now()), utc);
                        ps.setString(6, leaseId);
                        
                        // Note that if we tried to update the row but no rows were affected,
                        // it means that we were unable to obtain the lease because it is held by another
                        // instance (and hasn't expired), hence result == FALSE
                        int rows = ps.executeUpdate();
                        if (rows == 1) {
                            result = Boolean.TRUE; // row updated, therefore we own the lease
                        }
                    } catch (SQLException x) {
                        throw translator.translate(x);
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * Debug utility to inspect the content of the control table
     * @param translator
     * @param c
     */
    @SuppressWarnings("unused")
    private void dumpLeaseTable(String prefix, IDatabaseTranslator translator, Connection c) {
        final Calendar utc = CalendarHelper.getCalendarForUTC();
        final String CONTROL = DataDefinitionUtil.getQualifiedName(adminSchema, SchemaConstants.CONTROL);
        final String SQL = ""
                + " SELECT schema_name, lease_owner_host, lease_owner_uuid, "
                + "        lease_until "
                + "   FROM " + CONTROL;
        
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            while (rs.next()) {
                String row = String.format("%s: %s %s %s %s", prefix,
                    rs.getString(1), rs.getString(2), rs.getString(3), 
                    rs.getTimestamp(4, utc).toInstant().toString());
                logger.info(row);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}