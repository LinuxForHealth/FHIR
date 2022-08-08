/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.reference;

import java.util.List;

import org.linuxforhealth.fhir.cql.helpers.ParameterMap;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class MeasureSubmitDataOperation extends AbstractOperation {

    public static final String PARAM_IN_MEASURE_REPORT = "measureReport";
    public static final String PARAM_IN_RESOURCE = "resource";
    public static final String PARAM_OUT_RETURN ="return";

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Measure-submit-data", OperationDefinition.class);
    }

    @Override
    public Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {

        ParameterMap paramMap = new ParameterMap(parameters);

        Parameter param = paramMap.getSingletonParameter(PARAM_IN_MEASURE_REPORT);
        MeasureReport measureReport = (MeasureReport) param.getResource();

        List<Parameter> resources = paramMap.getParameter(PARAM_IN_RESOURCE);

        Bundle.Builder builder = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry( createEntry(measureReport) );

        if( resources != null ) {
            resources.stream()
                .map(p -> p.getResource())
                .forEach(r -> builder.entry(createEntry(r)));
        }

        try {
            Bundle bundle = builder.build();

            Bundle response = resourceHelper.doBundle(bundle, false);

            return FHIROperationUtil.getOutputParameters(PARAM_OUT_RETURN, response);

        } catch( FHIROperationException fex ) {
            throw fex;
        } catch( Exception ex ) {
            throw new FHIROperationException("Operation failed", ex);
        }
    }

    /**
     * Create a bundle entry for the provided resource
     *
     * @param resource
     *            FHIR resource
     * @return Bundle entry
     */
    protected Bundle.Entry createEntry( Resource resource ) {
        // the cqf-ruler implementation uses POST, but the operation definition says that
        // this operation needs to be idempotent. In order to be idempotent, I believe this
        // needs to be a PUT. Otherwise, we are going to end up with multiple copies of the
        // submitted resource.

        Bundle.Entry.Request request = null;
        if( resource.getId() != null ) {
            request = Bundle.Entry.Request.builder()
                .method( HTTPVerb.PUT )
                .url( Uri.of( reference(resource).getReference().getValue() ) )
                .build();
        } else {
            request = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url( Uri.of("/" + resource.getClass().getSimpleName()) )
                .build();
        }

        if( resource.getMeta() != null ) {
            resource = resource.toBuilder()
                    .meta(resource.getMeta().toBuilder()
                        .versionId(null)
                        .build())
                    .build();
        }

        return Bundle.Entry.builder()
            .resource(resource)
            .request(request)
            .build();
    }
}