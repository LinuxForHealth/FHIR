/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.util.List;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchExtension;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchQueryVisitor;
import org.linuxforhealth.fhir.search.parameters.InclusionParameter;

/**
 * SearchExtension for building _revinclude queries
 */
public class RevIncludeExtension implements SearchExtension {

    private final InclusionParameter inclusionParm;

    private final List<Long> ids;

    public RevIncludeExtension(InclusionParameter inclusionParm, List<Long> ids) {
        this.inclusionParm = inclusionParm;
        this.ids = ids;
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        return visitor.addRevIncludeFilter(query, inclusionParm, ids);
    }
}