package com.ibm.fhir.cql.helpers;

import java.util.HashMap;
import java.util.Map;

import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;

import com.ibm.fhir.cql.Constants;
import com.ibm.fhir.cql.engine.model.FhirModelResolver;

public class DataProviderFactory {
    public static Map<String, DataProvider> createDataProviders(RetrieveProvider retrieveProvider) {
        String suffix = "";

        // This is a hack to get around the fact that IBM FHIR types are in multiple packages
        // and the CQL engine expects there to be only a single package name
        Map<String, DataProvider> dataProviders = new HashMap<>();
        for (String packageName : FhirModelResolver.ALL_PACKAGES) {
            ModelResolver modelResolver = new FhirModelResolver();
            modelResolver.setPackageName(packageName);

            DataProvider provider = new CompositeDataProvider(modelResolver, retrieveProvider);
            dataProviders.put(Constants.FHIR_NS_URI + suffix, provider);
            suffix += "-";
        }
        return dataProviders;
    }
}
