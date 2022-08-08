/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ICommonValuesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValueResult;

/**
 * DAO to fetch common value records normalized in common_token_values,
 * common_canonical_values and code_systems
 */
public class CommonValuesDAO implements ICommonValuesDAO {
    private static final Logger logger = Logger.getLogger(CommonValuesDAO.class.getName());

    private final String schemaName;

    // hold on to the connection because we use batches to improve efficiency
    private final Connection connection;

    // The translator for the type of database we are connected to
    private final IDatabaseTranslator translator;

    /**
     * Public constructor
     * 
     * @param t
     * @param c
     * @param schemaName
     */
    public CommonValuesDAO(IDatabaseTranslator t, Connection c, String schemaName) {
        this.translator = t;
        this.schemaName = schemaName;
        this.connection = c;
    }

    /**
     * Getter for the {@link IDatabaseTranslator} held by this DAO
     * @return
     */
    protected IDatabaseTranslator getTranslator() {
        return this.translator;
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
    public Long readCanonicalId(String canonicalValue) {
        Long result;
        final String SQL = ""
                + "SELECT canonical_id "
                + "  FROM common_canonical_values "
                + " WHERE url = ? ";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, canonicalValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
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
}