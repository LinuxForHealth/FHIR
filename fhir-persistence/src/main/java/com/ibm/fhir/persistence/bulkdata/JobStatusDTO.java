/* 
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.bulkdata;

public class JobStatusDTO {
    public boolean shouldContinue = false;
    public boolean complete = false;
    public String blob = "";
    public String intJobId = "";
}
