/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.util;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.DomainResource;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.Identifier;
import com.ibm.watsonhealth.fhir.model.Person;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.replication.api.model.ReplicationInfo;

/**
 * Contains utility methods related to the FHIR Replication API.
 * @author markd
 *
 */
public class ReplicationUtil {
    
    private static final String EXTURL_STUDY_ID = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/1.0/studyid";
    private static final String EXTURL_PATIENT_ID = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/1.0/patientid";
    private static final String EXTURL_SITE_ID = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/1.0/siteid";
    private static final String EXTURL_APP_NAME = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/r1/appName";
    private static final String EXTURL_APP_VERSION = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/r1/appVersionNumber";
    private static final String EXTURL_RESOURCENAME = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/r1/resourceName";
    private static final String EXTURL_INTIDENTIFIER = "http://www.ibm.com/watsonhealth/fhir/extensions/whc-lsf/r1/intIdentifier";
    
    /**
     * Adds the first patientId, siteId, and studyId contained in the passed Resource to the ReplicationInfo contained in the 
     * passed persistence context. 
     * @param context A valid persistence context object
     * @param resource A valid FHIRResource
     */
    public static void addExtensionDataToRepInfo(FHIRPersistenceContext context, Resource resource) {
        ReplicationInfo repInfo;
        String patientId = null;
        String siteId = null;
        String studyId = null;
                 
        repInfo = (ReplicationInfo)context.getPersistenceEvent().getProperty(FHIRPersistenceEvent.PROPNAME_REPLICATION_INFO);
        if (repInfo != null) {
            patientId = FHIRUtil.getExtensionStringValue(resource, EXTURL_PATIENT_ID);
            if (patientId != null) {
                repInfo.setPatientId(patientId);
            }
            siteId = FHIRUtil.getExtensionStringValue(resource, EXTURL_SITE_ID);
            if (siteId != null) {
                repInfo.setSiteId(siteId);
            }
            studyId = FHIRUtil.getExtensionStringValue(resource, EXTURL_STUDY_ID);
            if (studyId != null) {
                repInfo.setStudyId(studyId);
            }
        }
    }


    /**
     * Add pertinent extensions from the copyFromResource to the copyToResource.
     * @param extensions A List of valid FHIR Resource extensions.
     * @param resource A valid FHIR Resource
     */
    public static void addExtensionDataToResource(Resource copyFromResource, Resource copyToResource) {
        DomainResource copyFromDomainResource, copyToDomainResource;
                
        if (copyFromResource instanceof DomainResource && copyToResource instanceof DomainResource) {
            copyFromDomainResource = (DomainResource) copyFromResource;
            copyToDomainResource = (DomainResource) copyToResource;
            
            for (Extension extension : copyFromDomainResource.getExtension()) {
                if (extension.getUrl().equals(EXTURL_SITE_ID) || 
                        extension.getUrl().equals(EXTURL_STUDY_ID) ||
                        extension.getUrl().equals(EXTURL_PATIENT_ID) ||
                        extension.getUrl().equals(EXTURL_APP_NAME) ||
                        extension.getUrl().equals(EXTURL_APP_VERSION) ||
                        extension.getUrl().equals(EXTURL_INTIDENTIFIER) ||
                        extension.getUrl().equals(EXTURL_RESOURCENAME)) {
                    copyToDomainResource.getExtension().add(extension);
                }
            }
            
            if (copyFromResource instanceof Person && copyToResource instanceof Person) {
                List<Identifier> identifiers = ((Person) copyFromResource).getIdentifier();
                for (Identifier identifier : identifiers) {
                    if (identifier.getSystem() != null && EXTURL_INTIDENTIFIER.equals(identifier.getSystem().getValue())) {
                        ((Person) copyToResource).getIdentifier().add(identifier);
                    }
                }
            }
        }
    }
}
