/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.util;

import java.util.List;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
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
	 * Adds the patientId, siteId and studyId contained in the passed Resource to the ReplicationInfo contained in the 
	 * passed persistence context. This is only done for Resource types defined in the "studyScopedResources"
	 * subsection of the fhir-server-config.json file. 
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
			if (isStudyScopedResourceType(resource)) {
				patientId = FHIRUtil.getExtensionStringValue(resource, EXTURL_PATIENT_ID);
				repInfo.setPatientId(patientId);
				siteId = FHIRUtil.getExtensionStringValue(resource, EXTURL_SITE_ID);
				repInfo.setSiteId(siteId);
				studyId = FHIRUtil.getExtensionStringValue(resource, EXTURL_STUDY_ID);
				repInfo.setStudyId(studyId);
			}
			else {
				if (isPatientRelatedResourceType(resource)) {
					patientId = FHIRUtil.getExtensionStringValue(resource, EXTURL_PATIENT_ID);
					repInfo.setPatientId(patientId);
				}
			}
		}
	}
	
	/**
	 * Add the required extensions in the passed List of extensions to the passed Resource.
	 * The required extensions are the ones relevant to study-scoped resource types and patient-related
	 * resource types.
	 * @param extensions A List of valid FHIR Resource extensions.
	 * @param resource A valid FHIR Resource
	 */
	public static void addExtensionDataToResource(Resource copyFromResource, Resource copyToResource) {
		DomainResource copyFromDomainResource, copyToDomainResource;
				
		if (copyFromResource instanceof DomainResource && copyToResource instanceof DomainResource) {
			copyFromDomainResource = (DomainResource) copyFromResource;
			copyToDomainResource = (DomainResource) copyToResource;
            
            for (Extension extension : copyFromDomainResource.getExtension()) {
				if (isStudyScopedResourceType(copyFromDomainResource) && 
					  (extension.getUrl().equals(EXTURL_SITE_ID) || 
						extension.getUrl().equals(EXTURL_STUDY_ID) ||
					    extension.getUrl().equals(EXTURL_PATIENT_ID) ||
					    extension.getUrl().equals(EXTURL_APP_NAME) ||
					    extension.getUrl().equals(EXTURL_APP_VERSION) ||
					    extension.getUrl().equals(EXTURL_INTIDENTIFIER)) ) {
					copyToDomainResource.getExtension().add(extension);
				}
				else if (isPatientRelatedResourceType(copyFromDomainResource) &&  
					     extension.getUrl().equals(EXTURL_PATIENT_ID)) {
					copyToDomainResource.getExtension().add(extension);
				} else if (extension.getUrl().equals(EXTURL_RESOURCENAME)) {
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
	
	/**
	 * Determines whether or not the passed Resource is a study-scoped resource type.
	 * @param resource A valid FHIR Resource
	 * @return boolean - true if the passed resource is a study-scoped type; false otherwise.
	 */
	private static boolean isStudyScopedResourceType(Resource resource) {
		boolean isStudyScopedResourceName = false;
		List<String> studyScopedResourceNames;
		
		studyScopedResourceNames = FHIRConfigHelper.getStringListProperty(FHIRConfiguration.PROPERTY_STUDY_SCOPED_RESOURCES);
		isStudyScopedResourceName = (studyScopedResourceNames != null) && 
							   	    (studyScopedResourceNames.contains(FHIRUtil.getExtensionStringValue(resource, EXTURL_RESOURCENAME)));
		
		return isStudyScopedResourceName;
		
	}
	
	/**
	 * Determines whether or not the passed Resource is a patient-related resource type.
	 * @param resource A valid FHIR Resource
	 * @return boolean - true if the passed resource is a patient-related type; false otherwise.
	 */
	private static boolean isPatientRelatedResourceType(Resource resource) {
		boolean isPatientRelatedResourceType = false;
		List<String> patientRelatedResourceTypes;
		
		patientRelatedResourceTypes = FHIRConfigHelper.getStringListProperty(FHIRConfiguration.PROPERTY_RESOURCE_TYPES_REQUIRING_SUBJECT_ID);
		isPatientRelatedResourceType = (patientRelatedResourceTypes != null) && 
			   	   					   (patientRelatedResourceTypes.contains(FHIRUtil.getExtensionStringValue(resource, EXTURL_RESOURCENAME)));
		
		return isPatientRelatedResourceType;
		
	}
}
