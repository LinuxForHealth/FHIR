/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.util;

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
}
