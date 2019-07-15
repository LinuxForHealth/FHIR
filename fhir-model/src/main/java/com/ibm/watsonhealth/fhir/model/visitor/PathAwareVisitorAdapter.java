/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;

public class PathAwareVisitorAdapter extends PathAwareAbstractVisitor {
    @Override
    protected void doVisitEnd(String elementName, Element element) {
        // do nothing
    }

    @Override
    protected void doVisitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        // do nothing
    }

    @Override
    protected void doVisitEnd(String elementName, Resource resource) {
        // do nothing
    }

    @Override
    protected void doVisitStart(String elementName, Element element) {
        // do nothing
    }

    @Override
    protected void doVisitStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        // do nothing
    }

    @Override
    protected void doVisitStart(String elementName, Resource resource) {
        // do nothing
    }
}
