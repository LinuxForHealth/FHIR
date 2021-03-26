/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.term.service.FHIRTermService;

public class ClosureTableSupport {
    private static final Map<String, Set<String>> CLOSURE_TABLE_CACHE = createLRUCache(1024);

    private ClosureTableSupport() { }

    public static Set<String> getClosureTable(CodeSystem codeSystem, Code code) {
        Objects.requireNonNull(codeSystem, "codeSystem");
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        Objects.requireNonNull(codeSystem.getUrl().getValue(), "CodeSystem.url.value");
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(code.getValue(), "Code.value");

        String url = (codeSystem.getVersion() != null && codeSystem.getVersion().getValue() != null) ?
                codeSystem.getUrl().getValue() + "|" + codeSystem.getVersion().getValue() :
                    codeSystem.getUrl().getValue();

        return CLOSURE_TABLE_CACHE.computeIfAbsent(url, k -> computeClosureTable(codeSystem, code));
    }

    private static Set<String> computeClosureTable(CodeSystem codeSystem, Code code) {
        Set<Concept> closure = FHIRTermService.getInstance().closure(codeSystem, code);
        if (closure == null) {
            return Collections.emptySet();
        }
        Set<String> result = new LinkedHashSet<>(closure.size());
        for (Concept concept : closure) {
            result.add(concept.getCode().getValue());
        }
        return result;
    }
}
