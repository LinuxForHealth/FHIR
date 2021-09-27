/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;

public abstract class AbstractTermServiceProvider implements FHIRTermServiceProvider {
    @Override
    public abstract Set<Concept> closure(CodeSystem codeSystem, Code code);

    @Override
    public abstract Concept getConcept(CodeSystem codeSystem, Code code);

    @Override
    public abstract Set<Concept> getConcepts(CodeSystem codeSystem);

    @Override
    public abstract Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters);

    @Override
    public abstract boolean hasConcept(CodeSystem codeSystem, Code code);

    @Override
    public abstract boolean isSupported(CodeSystem codeSystem);

    @Override
    public abstract boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB);

    private void checkArgument(Code code, String message) {
        requireNonNull(code, message);
        requireNonNull(code.getValue(), "Code.value");
    }

    private void checkArgument(Filter filter) {
        requireNonNull(filter, "filter");
        requireNonNull(filter.getProperty(), "Filter.property");
        requireNonNull(filter.getProperty().getValue(), "Filter.property.value");
        requireNonNull(filter.getOp(), "Filter.op");
        requireNonNull(filter.getOp().getValue(), "Filter.op.value");
        requireNonNull(filter.getValue(), "Filter.value");
        requireNonNull(filter.getValue().getValue(), "Filter.value.value");
    }

    private <R> void checkArgument(Function<Concept, ? extends R> function) {
        requireNonNull(function, "function");
    }

    protected void checkArgument(CodeSystem codeSystem) {
        requireNonNull(codeSystem, "codeSystem");
        requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        requireNonNull(codeSystem.getUrl().getValue(), "CodeSystem.url.value");
        if (codeSystem.getVersion() != null) {
            requireNonNull(codeSystem.getVersion().getValue(), "CodeSystem.version.value");
        }
    }

    protected void checkArguments(CodeSystem codeSystem, Code code) {
        checkArgument(codeSystem);
        checkArgument(code, "code");
    }

    protected void checkArguments(CodeSystem codeSystem, Code codeA, Code codeB) {
        checkArgument(codeSystem);
        checkArgument(codeA, "codeA");
        checkArgument(codeB, "codeB");
    }

    protected <R> void checkArguments(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        checkArgument(codeSystem);
        checkArgument(function);
    }

    protected void checkArguments(CodeSystem codeSystem, List<Filter> filters) {
        checkArgument(codeSystem);
        requireNonNull(filters, "filters");
        filters.forEach(filter -> checkArgument(filter));
    }

    protected <R> void checkArguments(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        checkArguments(codeSystem, filters);
        checkArgument(function);
    }
}