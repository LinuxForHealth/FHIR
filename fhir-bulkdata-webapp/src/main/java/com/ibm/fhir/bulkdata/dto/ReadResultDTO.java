/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.model.resource.Resource;

/**
 * ReadResultDTO enables a cleaner interface between data transferring between
 * Read/Write
 */
public class ReadResultDTO implements Serializable {

    private static final long serialVersionUID = 7196860944818539455L;

    private List<Resource> resources = new ArrayList<>();

    public ReadResultDTO() {
        // No Operation
    }

    public ReadResultDTO(List<Resource> resources) {
        this.resources.addAll(resources);
    }

    /**
     * @return the resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * @param resources
     *            the resources to set
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    public int size() {
        return resources.size();
    }

    public boolean empty() {
        return resources.isEmpty();
    }
}