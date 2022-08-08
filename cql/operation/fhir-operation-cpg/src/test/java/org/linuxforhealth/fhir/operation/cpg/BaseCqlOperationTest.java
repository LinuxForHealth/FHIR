/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cpg;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.testng.annotations.BeforeMethod;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.search.util.SearchHelper;

public abstract class BaseCqlOperationTest<OT extends AbstractCqlOperation> {

    AbstractCqlOperation op;

    protected java.util.Date start;
    protected java.util.Date end;

    protected SearchHelper searchHelper = new SearchHelper();

    protected abstract OT getOperation();

    @BeforeMethod
    public void setup() {
        op = getOperation();

        Calendar c = Calendar.getInstance();
        c.set(2000, 03, 04, 05, 06, 07);
        end = c.getTime();
        c.add(Calendar.YEAR, -1);
        start = c.getTime();
    }

    public Map<String, Object> getDefaultCqlParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Measurement Period", new Interval(Date.fromJavaDate(start), true, Date.fromJavaDate(end), true));
        return parameters;
    }

    @SuppressWarnings("unchecked")
    protected SingleResourceResult<? extends Resource> asResult(Resource patient) {
        SingleResourceResult<Resource> result = mock(SingleResourceResult.class);
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(patient);
        return result;
    }
}
