/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * This class is used to manage access to build-related information stored
 * during the build in buildinfo.properties.
 */
public class FHIRBuildIdentifier {
    private static final String CLASSNAME = FHIRBuildIdentifier.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);
    
    private static final String BUILD_PROPS_FILENAME = "buildinfo.fhirserver.properties";
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
                    log.severe("All attempts to open input stream to " + BUILD_PROPS_FILENAME + " have failed." );
                }
            }
        }
        
        if (is != null) {
            try {
                buildProperties.load(is);
                log.info("Build properties file successfully loaded: " + BUILD_PROPS_FILENAME);
                log.info("Build properties file contains: \n" + buildProperties.toString());
            } catch (Throwable e) {
                String msg = "Attempt to load build properties from input stream failed.";
                FHIROperationException fe = new FHIROperationException(msg, e);
                log.log(Level.SEVERE, msg, fe);
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
