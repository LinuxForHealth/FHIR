/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service;

import static org.linuxforhealth.fhir.term.service.util.FHIRTermServiceUtil.getParameter;
import static org.linuxforhealth.fhir.term.service.util.FHIRTermServiceUtil.getParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;

/**
 * This class is used to represent the optional input parameters of the expand operation:
 * <a href="http://hl7.org/fhir/valueset-operation-expand.html">http://hl7.org/fhir/valueset-operation-expand.html</a>
 */
public class ExpansionParameters {
    public static final ExpansionParameters EMPTY = ExpansionParameters.builder().build();

    private final Uri context;
    private final Code contextDirection;
    private final String filter;
    private final DateTime date;
    private final Integer offset;
    private final Integer count;
    private final Boolean includeDesignations;
    private final Boolean activeOnly;
    private final Boolean excludeNested;
    private final Boolean excludeNotForUI;
    private final Boolean excludePostCoordinated;
    private final Code displayLanguage;
    private final List<Canonical> excludeSystem;
    private final List<Canonical> systemVersion;
    private final List<Canonical> checkSystemVersion;
    private final List<Canonical> forceSystemVersion;

    private ExpansionParameters(Builder builder) {
        context = builder.context;
        contextDirection = builder.contextDirection;
        filter = builder.filter;
        date = builder.date;
        offset = builder.offset;
        count = builder.count;
        includeDesignations = builder.includeDesignations;
        activeOnly = builder.activeOnly;
        excludeNested = builder.excludeNested;
        excludeNotForUI = builder.excludeNotForUI;
        excludePostCoordinated = builder.excludePostCoordinated;
        displayLanguage = builder.displayLanguage;
        excludeSystem = Collections.unmodifiableList(builder.excludeSystem);
        systemVersion = Collections.unmodifiableList(builder.systemVersion);
        checkSystemVersion = Collections.unmodifiableList(builder.checkSystemVersion);
        forceSystemVersion = Collections.unmodifiableList(builder.forceSystemVersion);
    }

    public Uri getContext() {
        return context;
    }

    public Code getContextDirection() {
        return contextDirection;
    }

    public String getFilter() {
        return filter;
    }

    public DateTime getDate() {
        return date;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean getIncludeDesignations() {
        return includeDesignations;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public Boolean getExcludeNested() {
        return excludeNested;
    }

    public Boolean getExcludeNotForUI() {
        return excludeNotForUI;
    }

    public Boolean getExcludePostCoordinated() {
        return excludePostCoordinated;
    }

    public Code getDisplayLanguage() {
        return displayLanguage;
    }

    public List<Canonical> getExcludeSystem() {
        return excludeSystem;
    }

    public List<Canonical> getSystemVersion() {
        return systemVersion;
    }

    public List<Canonical> getCheckSystemVersion() {
        return checkSystemVersion;
    }

    public List<Canonical> getForceSystemVersion() {
        return forceSystemVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExpansionParameters other = (ExpansionParameters) obj;
        return Objects.equals(context, other.context) &&
                Objects.equals(contextDirection, other.contextDirection) &&
                Objects.equals(filter, other.filter) &&
                Objects.equals(date, other.date) &&
                Objects.equals(offset, other.offset) &&
                Objects.equals(count, other.count) &&
                Objects.equals(includeDesignations, other.includeDesignations) &&
                Objects.equals(activeOnly, other.activeOnly) &&
                Objects.equals(excludeNested, other.excludeNested) &&
                Objects.equals(excludeNotForUI, other.excludeNotForUI) &&
                Objects.equals(excludePostCoordinated, other.excludePostCoordinated) &&
                Objects.equals(displayLanguage, other.displayLanguage) &&
                Objects.equals(excludeSystem, other.excludeSystem) &&
                Objects.equals(systemVersion, other.systemVersion) &&
                Objects.equals(checkSystemVersion, other.checkSystemVersion) &&
                Objects.equals(forceSystemVersion, other.forceSystemVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context,
            contextDirection,
            filter,
            date,
            offset,
            count,
            includeDesignations,
            activeOnly,
            excludeNested,
            excludeNotForUI,
            excludePostCoordinated,
            displayLanguage,
            excludeSystem,
            systemVersion,
            checkSystemVersion,
            forceSystemVersion);
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ExpansionParameters from(Parameters parameters) {
        Parameter context = getParameter(parameters, "context");
        Parameter contextDirection = getParameter(parameters, "contextDirection");
        Parameter filter = getParameter(parameters, "filter");
        Parameter date = getParameter(parameters, "date");
        Parameter offset = getParameter(parameters, "offset");
        Parameter count = getParameter(parameters, "count");
        Parameter includeDesignations = getParameter(parameters, "includeDesignations");
        Parameter activeOnly = getParameter(parameters, "activeOnly");
        Parameter excludeNested = getParameter(parameters, "excludeNested");
        Parameter excludeNotForUI = getParameter(parameters, "excludeNotForUI");
        Parameter excludePostCoordinated = getParameter(parameters, "excludePostCoordinated");
        Parameter displayLanguage = getParameter(parameters, "displayLanguage");
        return ExpansionParameters.builder()
                .context((context != null) ? context.getValue().as(Uri.class) : null)
                .contextDirection((contextDirection != null) ? contextDirection.getValue().as(Code.class) : null)
                .filter((filter != null) ? filter.getValue().as(String.class) : null)
                .date((date != null) ? date.getValue().as(DateTime.class) : null)
                .offset((offset != null) ? offset.getValue().as(Integer.class) : null)
                .count((count != null) ? count.getValue().as(Integer.class) : null)
                .includeDesignations((includeDesignations != null) ? includeDesignations.getValue().as(Boolean.class) : null)
                .activeOnly((activeOnly != null) ? activeOnly.getValue().as(Boolean.class) : null)
                .excludeNested((excludeNested != null) ? excludeNested.getValue().as(Boolean.class) : null)
                .excludeNotForUI((excludeNotForUI != null) ? excludeNotForUI.getValue().as(Boolean.class) : null)
                .excludePostCoordinated((excludePostCoordinated != null) ? excludePostCoordinated.getValue().as(Boolean.class) : null)
                .displayLanguage((displayLanguage != null) ? displayLanguage.getValue().as(Code.class) : null)
                .excludeSystem(getParameters(parameters, "excludeSystem").stream()
                        .map(parameter -> parameter.getValue().as(Canonical.class))
                        .collect(Collectors.toList()))
                .systemVersion(getParameters(parameters, "systemVersion").stream()
                        .map(parameter -> parameter.getValue().as(Canonical.class))
                        .collect(Collectors.toList()))
                .checkSystemVersion(getParameters(parameters, "checkSystemVersion").stream()
                        .map(parameter -> parameter.getValue().as(Canonical.class))
                        .collect(Collectors.toList()))
                .forceSystemVersion(getParameters(parameters, "forceSystemVersion").stream()
                        .map(parameter -> parameter.getValue().as(Canonical.class))
                        .collect(Collectors.toList()))
                .build();
    }

    public static class Builder {
        private Uri context;
        private Code contextDirection;
        private String filter;
        private DateTime date;
        private Integer offset;
        private Integer count;
        private Boolean includeDesignations;
        private Boolean activeOnly;
        private Boolean excludeNested;
        private Boolean excludeNotForUI;
        private Boolean excludePostCoordinated;
        private Code displayLanguage;
        private List<Canonical> excludeSystem = new ArrayList<>();
        private List<Canonical> systemVersion = new ArrayList<>();
        private List<Canonical> checkSystemVersion = new ArrayList<>();
        private List<Canonical> forceSystemVersion = new ArrayList<>();

        private Builder() { }

        public Builder context(Uri context) {
            this.context = context;
            return this;
        }

        public Builder contextDirection(Code contextDirection) {
            this.contextDirection = contextDirection;
            return this;
        }

        public Builder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public Builder includeDesignations(Boolean includeDesignations) {
            this.includeDesignations = includeDesignations;
            return this;
        }

        public Builder activeOnly(Boolean activeOnly) {
            this.activeOnly = activeOnly;
            return this;
        }

        public Builder excludeNested(Boolean excludeNested) {
            this.excludeNested = excludeNested;
            return this;
        }

        public Builder excludeNotForUI(Boolean excludeNotForUI) {
            this.excludeNotForUI = excludeNotForUI;
            return this;
        }

        public Builder excludePostCoordinated(Boolean excludePostCoordinated) {
            this.excludePostCoordinated = excludePostCoordinated;
            return this;
        }

        public Builder displayLanguage(Code displayLanguage) {
            this.displayLanguage = displayLanguage;
            return this;
        }

        public Builder excludeSystem(Canonical... excludeSystem) {
            for (Canonical value : excludeSystem) {
                this.excludeSystem.add(value);
            }
            return this;
        }

        public Builder excludeSystem(Collection<Canonical> excludeSystem) {
            this.excludeSystem = new ArrayList<>(excludeSystem);
            return this;
        }

        public Builder systemVersion(Canonical... systemVersion) {
            for (Canonical value : systemVersion) {
                this.systemVersion.add(value);
            }
            return this;
        }

        public Builder systemVersion(Collection<Canonical> systemVersion) {
            this.systemVersion = new ArrayList<>(systemVersion);
            return this;
        }

        public Builder checkSystemVersion(Canonical... checkSystemVersion) {
            for (Canonical value : checkSystemVersion) {
                this.checkSystemVersion.add(value);
            }
            return this;
        }

        public Builder checkSystemVersion(Collection<Canonical> checkSystemVersion) {
            this.checkSystemVersion = new ArrayList<>(checkSystemVersion);
            return this;
        }

        public Builder forceSystemVersion(Canonical... forceSystemVersion) {
            for (Canonical value : forceSystemVersion) {
                this.forceSystemVersion.add(value);
            }
            return this;
        }

        public Builder forceSystemVersion(Collection<Canonical> forceSystemVersion) {
            this.forceSystemVersion = new ArrayList<>(excludeSystem);
            return this;
        }

        public ExpansionParameters build() {
            return new ExpansionParameters(this);
        }

        protected Builder from(ExpansionParameters expansionParameters) {
            context = expansionParameters.context;
            contextDirection = expansionParameters.contextDirection;
            filter = expansionParameters.filter;
            date = expansionParameters.date;
            offset = expansionParameters.offset;
            count = expansionParameters.count;
            includeDesignations = expansionParameters.includeDesignations;
            activeOnly = expansionParameters.activeOnly;
            excludeNested = expansionParameters.excludeNested;
            excludeNotForUI = expansionParameters.excludeNotForUI;
            excludePostCoordinated = expansionParameters.excludePostCoordinated;
            displayLanguage = expansionParameters.displayLanguage;
            excludeSystem.addAll(expansionParameters.excludeSystem);
            systemVersion.addAll(expansionParameters.systemVersion);
            checkSystemVersion.addAll(expansionParameters.checkSystemVersion);
            forceSystemVersion.addAll(expansionParameters.forceSystemVersion);
            return this;
        }
    }
}
