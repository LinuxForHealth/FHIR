/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Fetch all the rows from the SYSCS_DIAG.LOCK_TABLE
 */
public class DerbyLockDiag implements IDatabaseSupplier<List<LockInfo>> {

    private final String SQL = ""
            + " SELECT xid, type, mode, tablename, lockname, state, tabletype, lockcount, indexname "
            + "   FROM SYSCS_DIAG.LOCK_TABLE";
    
    /**
     * Public constructor
     */
    public DerbyLockDiag() {
        // NOP
    }

    @Override
    public List<LockInfo> run(IDatabaseTranslator translator, Connection c) {

        List<LockInfo> result = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LockInfo info = new LockInfo(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9));
                result.add(info);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }
}
