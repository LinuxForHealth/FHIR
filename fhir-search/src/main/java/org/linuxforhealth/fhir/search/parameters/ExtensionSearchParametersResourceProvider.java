/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.parameters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.spi.AbstractRegistryResourceProvider;
import org.linuxforhealth.fhir.search.parameters.cache.TenantSpecificSearchParameterCache;

/**
 * A FHIRRegistryResourceProvider that provides SearchParameter resources from tenant configuration files
 * (extension-search-parameters.json by default).
 */
public class ExtensionSearchParametersResourceProvider extends AbstractRegistryResourceProvider {
    private static final Logger log = Logger.getLogger(ExtensionSearchParametersResourceProvider.class.getName());

    private static final String NO_TENANT_SP_MAP_LOGGING =
            "No tenant-specific search parameters found for tenant '%s'; trying '%s'";

    /*
     * This is our in-memory cache of SearchParameter objects. The cache is organized at the top level by tenant-id.
     * SearchParameters contained in the default tenant's extension-search-parameters.json file are stored under the
     * "default" tenant-id, and other tenants' SearchParameters (defined in their tenant-specific
     * extension-search-parameters.json files) will be stored under their respective tenant-ids as well. The objects
     * stored in our cache are of type CachedObjectHolder, with each one containing a List<SearchParameter>.
     */
    private static TenantSpecificSearchParameterCache searchParameterCache = new TenantSpecificSearchParameterCache();

    @Override
    protected List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url) {
        if (resourceType != SearchParameter.class || url == null) {
            return Collections.emptyList();
        }

        String tenantId = FHIRRequestContext.get().getTenantId();
        return getTenantOrDefaultExtensionSPs(tenantId).stream()
                .filter(sp -> url.equals(sp.getUrl().getValue()))
                .map(sp -> FHIRRegistryResource.from(sp))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        String tenantId = FHIRRequestContext.get().getTenantId();
        return getTenantOrDefaultExtensionSPs(tenantId).stream()
                .map(sp -> FHIRRegistryResource.from(sp))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        if (resourceType != SearchParameter.class) {
            return Collections.emptyList();
        }

        return getRegistryResources();
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptySet();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        Objects.requireNonNull(type);

        return getRegistryResources().stream()
                .filter(rr -> type.equals(rr.getType()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the extended search parameters for the specified tenant,
     * the default tenant (if no tenant-specific extension search parameter file exists),
     * or an empty list (if there are no extended search parameters or we hit an error while trying to get them).
     *
     * @param tenantId
     *     the tenant whose SearchParameters should be returned.
     * @return a list of SearchParameter objects
     */
    private static List<SearchParameter> getTenantOrDefaultExtensionSPs(String tenantId) {
        List<SearchParameter> cachedObjectForTenant = null;

        try {
            cachedObjectForTenant = searchParameterCache.getCachedObjectForTenant(tenantId);

            if (cachedObjectForTenant == null) {

                if (log.isLoggable(Level.FINE)) {
                    log.fine(String.format(NO_TENANT_SP_MAP_LOGGING, tenantId, FHIRConfiguration.DEFAULT_TENANT_ID));
                }

                cachedObjectForTenant = searchParameterCache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error while loading extension search parameters for tenant " + tenantId, e);
        }

        return cachedObjectForTenant == null ? Collections.emptyList() : cachedObjectForTenant;
    }
}
