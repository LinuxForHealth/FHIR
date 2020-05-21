/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/*
CREATE TABLE concept (
  concept_id            INTEGER         NOT NULL ,
  concept_name          VARCHAR(255)    NOT NULL ,
  domain_id             VARCHAR(20)     NOT NULL ,
  vocabulary_id         VARCHAR(20)     NOT NULL ,
  concept_class_id      VARCHAR(20)     NOT NULL ,
  standard_concept      VARCHAR(1)      NULL ,
  concept_code          VARCHAR(50)     NOT NULL ,
  valid_start_date      DATE            NOT NULL ,
  valid_end_date        DATE            NOT NULL ,
  invalid_reason        VARCHAR(1)      NULL
)
;
 */
public class Concept {
    private final int conceptId;
    private final String conceptName;
    private final String domainId;
    private final String vocabularyId;
    private final String conceptClassId;
    private final String standardConcept;
    private final String conceptCode;
    private final Date validStartDate;
    private final Date validEndDate;
    private final String invalidReason;

    private Concept(
            int conceptId,
            String conceptName,
            String domainId,
            String vocabularyId,
            String conceptClassId,
            String standardConcept,
            String conceptCode,
            Date validStartDate,
            Date validEndDate,
            String invalidReason) {
        this.conceptId = Objects.requireNonNull(conceptId);
        this.conceptName = Objects.requireNonNull(conceptName);
        this.domainId = Objects.requireNonNull(domainId);
        this.vocabularyId = Objects.requireNonNull(vocabularyId);
        this.conceptClassId = Objects.requireNonNull(conceptClassId);
        this.standardConcept = standardConcept; // nullable
        this.conceptCode = Objects.requireNonNull(conceptCode);
        this.validStartDate = Objects.requireNonNull(validStartDate);
        this.validEndDate = Objects.requireNonNull(validEndDate);
        this.invalidReason = invalidReason; // nullable
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getConceptName() {
        return conceptName;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getVocabularyId() {
        return vocabularyId;
    }

    public String getConceptClassId() {
        return conceptClassId;
    }

    public String getStandardConcept() {
        return standardConcept;
    }

    public String getConceptCode() {
        return conceptCode;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public static Concept from(ResultSet resultSet) throws SQLException {
        Objects.requireNonNull(resultSet);
        return new Concept(
            resultSet.getInt("concept_id"),
            resultSet.getString("concept_name"),
            resultSet.getString("domain_id"),
            resultSet.getString("vocabulary_id"),
            resultSet.getString("concept_class_id"),
            resultSet.getString("standard_concept"),
            resultSet.getString("concept_code"),
            resultSet.getDate("valid_start_date"),
            resultSet.getDate("valid_end_date"),
            resultSet.getString("invalid_reason"));
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
        Concept other = (Concept) obj;
        return Objects.equals(conceptId, other.conceptId) &&
                Objects.equals(conceptName, other.conceptName) &&
                Objects.equals(domainId, other.domainId) &&
                Objects.equals(vocabularyId, other.vocabularyId) &&
                Objects.equals(conceptClassId, other.conceptClassId) &&
                Objects.equals(standardConcept, other.standardConcept) &&
                Objects.equals(conceptCode, other.conceptCode) &&
                Objects.equals(validStartDate, other.validStartDate) &&
                Objects.equals(validEndDate, other.validEndDate) &&
                Objects.equals(invalidReason, other.invalidReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            conceptId,
            conceptName,
            domainId,
            vocabularyId,
            conceptClassId,
            standardConcept,
            conceptCode,
            validStartDate,
            validEndDate,
            invalidReason);
    }

    @Override
    public String toString() {
        return "Concept [conceptId=" + conceptId +
                ", conceptName=" + conceptName +
                ", domainId=" + domainId +
                ", vocabularyId=" + vocabularyId +
                ", conceptClassId=" + conceptClassId +
                ", standardConcept=" + standardConcept +
                ", conceptCode=" + conceptCode +
                ", validStartDate=" + validStartDate +
                ", validEndDate=" + validEndDate +
                ", invalidReason=" + invalidReason + "]";
    }
}