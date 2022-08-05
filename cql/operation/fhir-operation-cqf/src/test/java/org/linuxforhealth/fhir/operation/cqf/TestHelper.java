/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;

public class TestHelper {
    
    public static Resource getTestResource(String path) throws Exception {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if( is != null ) {
                return FHIRParser.parser(Format.JSON).parse(is);
            } else {
                throw new IllegalArgumentException("No resource found with at path " + path);
            }
        }
    }
    
    public static <T> List<T> getBundleResources(String bundlePath, Class<T> clazz) throws Exception {
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(bundlePath)) {
            final Bundle bundle = (Bundle) FHIRParser.parser(Format.JSON).parse(is);

            return getBundleResources( bundle, clazz );
        }
    }
    
    public static <T> List<T> getBundleResources(Bundle bundle, Class<T> clazz) {
        return bundle.getEntry().stream().map(e -> e.getResource()).filter(r -> clazz.isInstance(r)).map(r -> clazz.cast(r) ).collect(Collectors.toList());
    }
    
    @SuppressWarnings("unchecked")
    public static SingleResourceResult<? extends Resource> asResult(Resource resource) {
        SingleResourceResult<Resource> result = (SingleResourceResult<Resource>) mock(SingleResourceResult.class);
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(resource);
        return result;
    }
}
