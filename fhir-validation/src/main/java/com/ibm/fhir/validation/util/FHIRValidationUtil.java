/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        List<Issue> errors = new ArrayList<>();
        for (Issue issue : issues) {
            if (IssueSeverity.ERROR.equals(issue.getSeverity())) {
                errors.add(issue);
            }
        }
        return errors;
    }

    public static List<Issue> getWarnings(List<Issue> issues) {
        Objects.requireNonNull(issues);
        List<Issue> warnings = new ArrayList<>();
        for (Issue issue : issues) {
            if (IssueSeverity.WARNING.equals(issue.getSeverity())) {
                warnings.add(issue);
            }
        }
        return warnings;
    }

    public static List<Issue> getInformation(List<Issue> issues) {
        Objects.requireNonNull(issues);
        List<Issue> information = new ArrayList<>();
        for (Issue issue : issues) {
            if (IssueSeverity.INFORMATION.equals(issue.getSeverity())) {
                information.add(issue);
            }
        }
        return information;
    }

    public static boolean hasErrors(List<Issue> issues) {
        for (Issue issue : issues) {
            if (IssueSeverity.ERROR.equals(issue.getSeverity())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasWarnings(List<Issue> issues) {
        for (Issue issue : issues) {
            if (IssueSeverity.WARNING.equals(issue.getSeverity())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasInformation(List<Issue> issues) {
        for (Issue issue : issues) {
            if (IssueSeverity.INFORMATION.equals(issue.getSeverity())) {
                return true;
            }
        }
        return false;
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
