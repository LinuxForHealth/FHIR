/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueSeverity;

public final class FHIRValidationUtil {
    public static final Comparator<Issue> ISSUE_COMPARATOR = new Comparator<Issue>() {
        @Override
        public int compare(Issue first, Issue second) {
            return first.getSeverity().getValueAsEnum().compareTo(second.getSeverity().getValueAsEnum());
        }
    };

    private FHIRValidationUtil() { }

    public static List<Issue> getErrors(List<Issue> issues) {
        Objects.requireNonNull(issues);
        return issues.stream()
                .filter(issue -> IssueSeverity.ERROR.equals(issue.getSeverity()))
                .collect(Collectors.toList());
    }

    public static List<Issue> getWarnings(List<Issue> issues) {
        Objects.requireNonNull(issues);
        return issues.stream()
                .filter(issue -> IssueSeverity.WARNING.equals(issue.getSeverity()))
                .collect(Collectors.toList());
    }

    public static List<Issue> getInformation(List<Issue> issues) {
        Objects.requireNonNull(issues);
        return issues.stream()
                .filter(issue -> IssueSeverity.INFORMATION.equals(issue.getSeverity()))
                .collect(Collectors.toList());
    }

    public static boolean hasErrors(List<Issue> issues) {
        return !getErrors(issues).isEmpty();
    }

    public static boolean hasWarnings(List<Issue> issues) {
        return !getWarnings(issues).isEmpty();
    }

    public static boolean hasInformation(List<Issue> issues) {
        return !getInformation(issues).isEmpty();
    }

    public static int countErrors(List<Issue> issues) {
        return getErrors(issues).size();
    }

    public static int countWarnings(List<Issue> issues) {
        return getWarnings(issues).size();
    }

    public static int countInformation(List<Issue> issues) {
        return getInformation(issues).size();
    }
}
