/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.cql.engine.server.terminology;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhircode;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.service.LookupOutcome;
import com.ibm.fhir.term.service.ValidationOutcome;
import com.ibm.fhir.term.util.ValueSetSupport;

public class ServerFhirTerminologyProvider implements TerminologyProvider {

    private static final String URN_UUID = "urn:uuid:";
    private static final String URN_OID = "urn:oid:";

    @Override
    public boolean in(Code code, ValueSetInfo valueSetInfo) {
        boolean result = false;

        ValueSet valueSet = resolveByUrl(valueSetInfo);
        if (valueSet != null) {
            ValidationOutcome outcome = FHIRTermService.getInstance().validateCode(valueSet, fhircode(code));
            result = outcome.getResult().getValue();
        }

        return result;
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSetInfo) {
        List<Code> codes;

        ValueSet valueSet = resolveByUrl(valueSetInfo);
        if (valueSet != null) {

            ValueSet expanded = FHIRTermService.getInstance().expand(valueSet);

            codes = new ArrayList<>();
            for (ValueSet.Expansion.Contains codeInfo : expanded.getExpansion().getContains()) {
                Code nextCode = new Code().withCode(codeInfo.getCode() != null ? codeInfo.getCode().getValue() : null).withSystem(codeInfo.getSystem() != null
                        ? codeInfo.getSystem().getValue() : null).withVersion(codeInfo.getVersion() != null ? codeInfo.getVersion().getValue()
                                : null).withDisplay(codeInfo.getDisplay() != null ? codeInfo.getDisplay().getValue() : null);
                codes.add(nextCode);
            }
        } else {
            codes = Collections.emptyList();
        }

        return codes;
    }

    @Override
    public Code lookup(Code code, CodeSystemInfo codeSystem) {
        LookupOutcome outcome = FHIRTermService.getInstance().lookup(Uri.of(codeSystem.getId()), fhirstring(codeSystem.getVersion()), fhircode(code));

        if (outcome.getDisplay() != null) {
            code.setDisplay(outcome.getDisplay().getValue());
        }

        return code.withSystem(codeSystem.getId());
    }

    private ValueSet resolveByUrl(ValueSetInfo valueSetInfo) {
        if (valueSetInfo.getVersion() != null
                || (valueSetInfo.getCodeSystems() != null && valueSetInfo.getCodeSystems().size() > 0)) {
            throw new UnsupportedOperationException(String.format("Could not expand value set %s; version and code system bindings are not supported at this time.", valueSetInfo.getId()));
        }

        StringBuilder url = new StringBuilder(valueSetInfo.getId());
        if (valueSetInfo.getVersion() != null) {
            url.append("|");
            url.append(valueSetInfo.getVersion());
        }

        ValueSet valueSet = ValueSetSupport.getValueSet(url.toString());
        if (valueSet == null) {
            String id = valueSetInfo.getId();
            if (id.startsWith(URN_OID)) {
                id = id.replace(URN_OID, "");
            } else if (id.startsWith(URN_UUID)) {
                id = id.replace(URN_UUID, "");
            }

            // If we reached this point and it looks like it might
            // be a FHIR resource ID, we will try to read it.
            // See https://www.hl7.org/fhir/datatypes.html#id
            if (id.matches("[A-Za-z0-9\\-\\.]{1,64}")) {
                // TODO - how to do the read?
            }
        }
        return valueSet;
    }
}
