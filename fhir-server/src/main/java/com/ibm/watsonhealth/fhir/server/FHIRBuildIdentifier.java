/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server;

import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to manage access to build-related information stored
 * during the build in buildinfo.properties.
 */
public class FHIRBuildIdentifier {
    
    private static final String BUILD_PROPS_FILENAME = "buildinfo.properties";
    private static final String BUILD_PROP_VERSION = "fhir.server.build.version";
    private static final String BUILD_PROP_BUILDID = "fhir.server.build.id";
    
    private static Properties buildProperties;
    
    static {
        buildProperties = new Properties();
        
        InputStream is = FHIRBuildIdentifier.class.getClassLoader().getResourceAsStream(BUILD_PROPS_FILENAME);
        if(is == null) {
            is = FHIRBuildIdentifier.class.getResourceAsStream("/" + BUILD_PROPS_FILENAME);
            if(is == null) {
                is = FHIRBuildIdentifier.class.getResourceAsStream(BUILD_PROPS_FILENAME);
                if(is == null) {
                    // throw new FHIRException("Failed to read build information: '" + BUILD_PROPS_FILENAME + "' not found");
                }
            }
        }
        
        if (is != null) {
            try {
                buildProperties.load(is);
            } catch (Exception e) {
                // throw new FHIRException("Failed to read build information: " + e.getMessage(), e);
            }
        }
    }
    
    public FHIRBuildIdentifier() {
    }
    
    public String getBuildVersion() {
        String version = buildProperties.getProperty(BUILD_PROP_VERSION, "unknown");
        if (version.endsWith("-SNAPSHOT")) {
            version = version.replace("-SNAPSHOT", "");
        }
        return version;
    }
    
    public String getBuildId() {
        return buildProperties.getProperty(BUILD_PROP_BUILDID);
    }

}
