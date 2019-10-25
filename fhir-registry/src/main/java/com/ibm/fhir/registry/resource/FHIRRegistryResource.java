/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.resource;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.loadResource;

import java.util.Comparator;
import java.util.Objects;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Resource;

public class FHIRRegistryResource {
    public static final Comparator<FHIRRegistryResource> VERSION_COMPARATOR = new Comparator<FHIRRegistryResource>() {
        @Override
        public int compare(FHIRRegistryResource first, FHIRRegistryResource second) {
            return first.version.compareTo(second.version);
        }
    };
    
    private final String url;
    private final Version version;
    private final String name;
    private final Format format;
    private final ClassLoader loader;
    
    private volatile Resource resource;
    
    public FHIRRegistryResource(String url, String version, String name, Format format, ClassLoader loader) {
        this.url = Objects.requireNonNull(url);
        this.version = Version.from(Objects.requireNonNull(version));
        this.name = Objects.requireNonNull(name);
        this.format = Objects.requireNonNull(format);
        this.loader = Objects.requireNonNull(loader);
    }
    
    public String getUrl() {
        return url;
    }
    
    public Version getVersion() {
        return version;
    }
    
    public Resource getResource() {
        Resource resource = this.resource;
        if (resource == null) {
            synchronized (this) {
                resource = this.resource;
                if (resource == null) {
                    resource = loadResource(name, format, loader);
                    this.resource = resource;
                }
            }
        }
        return resource;
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
    
    public static class Version implements Comparable<Version> {
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
}