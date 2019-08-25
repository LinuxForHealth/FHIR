/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.cadf.model;

/**
 * The CADF Attachment type is used as one means to add domain-specific
 * information to certain CADF entities or data types.
 */
public class CadfAttachment {
    /**
     * URI that identifies the data type of the content property. Required.
     */
    private String contentType;
    /**
     * A container with attachment data. Required.
     */
    private Object content;
    /**
     * An optional name that can be used to identify content (e.g. file name)
     */
    private String name;

    /**
     * Create an attachment object with a user-friendly name.
     *
     * @param contentType URI that identifies the data type of the content property.
     *                    Required. For example, an attachment that includes a
     *                    standard MIME types (such as "application/pdf") can be
     *                    included by setting this property to
     *                    "http://www.iana.org/assignments/media-types/application/pdf"
     * @param content     A container with attachment data. Required.
     * @param name        An optional name that can be used to identify content
     *                    (e.g. file name)
     */
    public CadfAttachment(String contentType, Object content, String name) {
        this.setContent(content);
        this.setContentType(contentType);
        this.setName(name);
    }

    /**
     * Create an attachment object.
     *
     * @param contentType URI that identifies the data type of the content property.
     *                    Required. For example, an attachment that includes a
     *                    standard MIME types (such as "application/pdf") can be
     *                    included by setting this property to
     *                    "http://www.iana.org/assignments/media-types/application/pdf"
     * @param content     A container with attachment data. Required.
     */
    public CadfAttachment(String contentType, Object content) {
        this.setContent(content);
        this.setContentType(contentType);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
