/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.vocab;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.ibm.fhir.omop.model.Concept;

public class VocabService {
    private static final Logger log = Logger.getLogger(VocabService.class.getName());

    private static final String GET_CONCEPT = "SELECT * FROM concept WHERE vocabulary_id = ? and concept_code = ?";
    private static final String GET_CONCEPTS = "SELECT * FROM concept WHERE vocabulary_id = ? ORDER BY concept_id LIMIT ? OFFSET ?";
    private final DataSource dataSource;
    private final static Map<ConceptKey, Concept> CONCEPT_CACHE = createLRUCache(1024);

    public VocabService(DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
    }

    public Concept getConcept(String vocabularyId, String conceptCode) {
        ConceptKey key = ConceptKey.key(vocabularyId, conceptCode);
        Concept concept = CONCEPT_CACHE.get(key);
        if (concept == null) {
            concept = CONCEPT_CACHE.computeIfAbsent(key, k -> getConcept(key));
        }
        return concept;
    }

    public List<Concept> getConcepts(String vocabularyId, int limit, int offset) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_CONCEPTS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, vocabularyId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                int rowCount = 0;
                if (resultSet.last()) {
                    rowCount = resultSet.getRow();
                    resultSet.beforeFirst();
                }
                List<Concept> concepts = new ArrayList<>(rowCount);
                while (resultSet.next()) {
                    concepts.add(Concept.from(resultSet));
                }
                return Collections.unmodifiableList(concepts);
            }
        } catch (SQLException e) {
            log.warning("The following error occurred during concept retrieval: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private Concept getConcept(ConceptKey key) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_CONCEPT)) {
            statement.setString(1, key.getVocabularyId());
            statement.setString(2, key.getConceptCode());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Concept.from(resultSet);
                }
            }
        } catch (SQLException e) {
            log.warning("The following error occurred during concept retrieval: " + e.getMessage());
        }
        return null;
    }

    public static class ConceptKey {
        private final String vocabularyId;
        private final String conceptCode;

        private ConceptKey(String vocabularyId, String conceptCode) {
            this.vocabularyId = Objects.requireNonNull(vocabularyId);
            this.conceptCode = Objects.requireNonNull(conceptCode);
        }

        public String getVocabularyId() {
            return vocabularyId;
        }

        public String getConceptCode() {
            return conceptCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ConceptKey other = (ConceptKey) obj;
            return Objects.equals(vocabularyId, other.vocabularyId) &&
                    Objects.equals(conceptCode, other.conceptCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vocabularyId, conceptCode);
        }

        public static ConceptKey key(String vocabularyId, String conceptCode) {
            return new ConceptKey(vocabularyId, conceptCode);
        }
    }
}
