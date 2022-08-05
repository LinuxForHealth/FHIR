/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.registry.resource;

import static org.linuxforhealth.fhir.registry.util.FHIRRegistryUtil.requireDefinitionalResourceType;

import java.util.Objects;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.registry.util.FHIRRegistryUtil;

/**
 * A base class that contains the metadata for a definitional resource (e.g. StructureDefinition)
 */
public class FHIRRegistryResource implements Comparable<FHIRRegistryResource> {
    private static final Logger log = Logger.getLogger(FHIRRegistryResource.class.getName());

    protected final Class<? extends Resource> resourceType;
    protected final String id;
    protected final String url;
    protected final Version version;
    protected final String kind;
    protected final String type;
    protected final boolean defaultVersion;

    protected volatile Resource resource;

    public FHIRRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type,
            boolean defaultVersion) {
        this.resourceType = Objects.requireNonNull(resourceType);
        this.id = id;
        this.url = Objects.requireNonNull(url);
        this.version = Objects.requireNonNull(version);
        this.kind = kind;
        this.type = type;
        this.defaultVersion = defaultVersion;
    }

    public FHIRRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type) {
        this(resourceType, id, url, version, kind, type, false);
    }

    public FHIRRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type,
            Resource resource,
            boolean defaultVersion) {
        this(resourceType, id, url, version, kind, type, defaultVersion);
        this.resource = resource;
    }

    public FHIRRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type,
            Resource resource) {
        this(resourceType, id, url, version, kind, type, false);
    }

    public Class<? extends Resource> getResourceType() {
        return resourceType;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Version getVersion() {
        return version;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public boolean isDefaultVersion() {
        return defaultVersion;
    }

    public Resource getResource() {
        return resource;
    }

    public <T extends FHIRRegistryResource> boolean is(Class<T> registryResourceType) {
        return registryResourceType.isInstance(this);
    }

    public <T extends FHIRRegistryResource> T as(Class<T> registryResourceType) {
        return registryResourceType.cast(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FHIRRegistryResource other = (FHIRRegistryResource) obj;
        return Objects.equals(url, other.url) && Objects.equals(version, other.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, version);
    }

    @Override
    public int compareTo(FHIRRegistryResource other) {
        int result = url.compareTo(other.url);
        if (result == 0) {
            return version.compareTo(other.version);
        }
        return result;
    }

    /**
     * Represents a version that can either be lexical or follow the Semantic Versioning format
     */
    public static class Version implements Comparable<Version> {
        public static final Version NO_VERSION = Version.from("<no version>");

        public enum CompareMode { SEMVER, LEXICAL };

        private final String version;
        private final Integer major;
        private final Integer minor;
        private final Integer patch;
        private final CompareMode mode;

        private Version(String version) {
            this.version = version;
            major = minor = patch = null;
            mode = CompareMode.LEXICAL;
        }

        private Version(String version, Integer major, Integer minor, Integer patch) {
            this.version = version;
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.mode = CompareMode.SEMVER;
        }

        public int major() {
            return major;
        }

        public int minor() {
            return minor;
        }

        public int patch() {
            return patch;
        }

        public static Version from(String version) {
            String[] tokens = version.split("\\.");
            if (tokens.length < 1 || tokens.length > 3) {
                return new Version(version);
            }
            try {
                Integer major = Integer.parseInt(tokens[0]);
                Integer minor = (tokens.length >= 2) ? Integer.parseInt(tokens[1]) : 0;
                Integer patch = (tokens.length == 3) ? Integer.parseInt(tokens[2]) : 0;
                return new Version(version, major, minor, patch);
            } catch (Exception e) {
                return new Version(version);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Version other = (Version) obj;
            if (CompareMode.LEXICAL.equals(mode) || CompareMode.LEXICAL.equals(other.mode)) {
                return Objects.equals(version, other.version);
            } else {
                return Objects.equals(major, other.major) && Objects.equals(minor, other.minor) && Objects.equals(patch, other.patch);
            }
        }

        @Override
        public int hashCode() {
            if (CompareMode.LEXICAL.equals(mode)) {
                return Objects.hash(version);
            } else {
                return Objects.hash(major, minor, patch);
            }
        }

        /**
         * @implNote returns the exact string in the version element of the corresponding resource
         */
        @Override
        public String toString() {
            return version;
        }

        @Override
        public int compareTo(Version version) {
            if (CompareMode.LEXICAL.equals(mode) || CompareMode.LEXICAL.equals(version.mode)) {
                return this.version.compareTo(version.version);
            } else {
                int result = major.compareTo(version.major);
                if (result == 0) {
                    result = minor.compareTo(version.minor);
                    if (result == 0) {
                        return patch.compareTo(version.patch);
                    }
                    return result;
                }
                return result;
            }
        }
    }

    public static FHIRRegistryResource from(Resource resource) {
        return from(resource, false);
    }

    public static FHIRRegistryResource from(Resource resource, boolean defaultVersion) {
        if (resource == null) {
            return null;
        }

        Class<? extends Resource> resourceType = resource.getClass();
        requireDefinitionalResourceType(resourceType);

        String id = resource.getId();
        String url = FHIRRegistryUtil.getUrl(resource);
        String version = FHIRRegistryUtil.getVersion(resource);
        if (url == null) {
            log.warning(String.format("Could not create FHIRRegistryResource from Resource with resourceType: %s, id: %s, url: %s, and version: %s", resourceType.getSimpleName(), id, url, version));
            return null;
        }

        String kind = null;
        String type = null;
        if (resource instanceof StructureDefinition) {
            StructureDefinition structureDefinition = (StructureDefinition) resource;
            kind = structureDefinition.getKind().getValue();
            type = structureDefinition.getType().getValue();
        } else if (resource instanceof SearchParameter) {
            SearchParameter searchParameter = (SearchParameter) resource;
            type = searchParameter.getType().getValue();
        }

        return new FHIRRegistryResource(resourceType, id, url, (version != null) ? Version.from(version) : Version.NO_VERSION, kind, type, resource, defaultVersion);
    }
}