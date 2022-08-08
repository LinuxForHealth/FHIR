/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.linuxforhealth.fhir.model.resource.Resource;

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

    public ReadResultDTO(List<? extends Resource> resources) {
        this.resources.addAll(resources);
    }

    /**
     * @return the resources
     */
    public List<? extends Resource> getResources() {
        return Collections.unmodifiableList(this.resources);
    }

    /**
     * Replace the contents of the internal resources list with the contents
     * of the given resources list
     * @param resources
     *            the resources to set
     */
    public void setResources(List<? extends Resource> resources) {
        this.resources.clear();
        this.resources.addAll(resources);
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