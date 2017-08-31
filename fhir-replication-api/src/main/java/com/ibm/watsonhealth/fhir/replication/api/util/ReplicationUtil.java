/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.util;

import java.util.List;

import static com.ibm.watsonhealth.fhir.replication.api.impl.ReplicationInfoInterceptor.*;
import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.model.DomainResource;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.replication.api.model.ReplicationInfo;

/**
 * Contains utility methods related to the FHIR Replication API.
 * @author markd
 *
 */
public class ReplicationUtil {
	
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
				patientId = getExtensionStringValue(resource, getSubjectIdExtensionUrl());
				repInfo.setPatientId(patientId);
				siteId = getExtensionStringValue(resource, getSiteIdExtensionUrl());
				repInfo.setSiteId(siteId);
				studyId = getExtensionStringValue(resource, getStudyIdExtensionUrl());
				repInfo.setStudyId(studyId);
			}
			else {
				if (isPatientRelatedResourceType(resource)) {
					patientId = getExtensionStringValue(resource, getSubjectIdExtensionUrl());
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
				
		if (DomainResource.class.isAssignableFrom(copyFromResource.getClass()) &&
			DomainResource.class.isAssignableFrom(copyToResource.getClass())) {
			copyFromDomainResource = (DomainResource) copyFromResource;
			copyToDomainResource = (DomainResource) copyToResource;
            
            for (Extension extension : copyFromDomainResource.getExtension()) {
				if (isStudyScopedResourceType(copyFromDomainResource) && 
					  (extension.getUrl().equals(getSiteIdExtensionUrl()) || 
						extension.getUrl().equals(getStudyIdExtensionUrl()) ||
					    extension.getUrl().equals(getSubjectIdExtensionUrl()))) {
						copyToDomainResource.getExtension().add(extension);
				}
				else if (isPatientRelatedResourceType(copyFromDomainResource) &&  
					     extension.getUrl().equals(getSubjectIdExtensionUrl())) {
						copyToDomainResource.getExtension().add(extension);
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
		boolean isStudyScopedResourceType = false;
		List<String> studyScopedResourceTypes;
		
		studyScopedResourceTypes = FHIRConfigHelper.getStringListProperty(FHIRConfiguration.PROPERTY_STUDY_SCOPED_RESOURCES);
		isStudyScopedResourceType = (studyScopedResourceTypes != null) && 
							   	    (studyScopedResourceTypes.contains(resource.getClass().getSimpleName()));
		
		return isStudyScopedResourceType;
		
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
			   	   					   (patientRelatedResourceTypes.contains(resource.getClass().getSimpleName()));
		
		return isPatientRelatedResourceType;
		
	}
}
