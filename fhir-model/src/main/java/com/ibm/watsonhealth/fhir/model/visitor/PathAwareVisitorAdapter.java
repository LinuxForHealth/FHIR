/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;

public class PathAwareVisitorAdapter extends PathAwareAbstractVisitor {
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        // do nothing
    }

    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        // do nothing
    }

    @Override
    protected void doVisitStart(String elementName, int elementIndex, Element element) {
        // do nothing
    }

    @Override
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
        // do nothing
    }
}
