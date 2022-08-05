/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.CommonValuesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValueResult;


/**
 * Derby-specific extension of the {@link CommonValuesDAO} to work around
 * some SQL syntax and Derby concurrency issues
 */
public class DerbyCommonValuesDAO extends CommonValuesDAO {
    private static final Logger logger = Logger.getLogger(DerbyCommonValuesDAO.class.getName());

    /**
     * Public constructor
     * @param t
     * @param c
     * @param schemaName
     */
    public DerbyCommonValuesDAO(IDatabaseTranslator t, Connection c, String schemaName) {
        super(t, c, schemaName);
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
}