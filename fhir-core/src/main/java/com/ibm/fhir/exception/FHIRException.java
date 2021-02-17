/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.exception;

import java.util.UUID;

import com.ibm.fhir.core.util.handler.IPHandler;

/**
 * Common FHIR Server exception base class.
 */
public class FHIRException extends Exception {
    private static final long serialVersionUID = 1L;

    private String uniqueId = null;

    /**
     * @see Exception#Exception()
     */
    public FHIRException() {
        super();
    }

    /**
     * @see Exception#Exception(String)
     */
    public FHIRException(String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public FHIRException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public FHIRException(Throwable cause) {
        super(cause);
    }

    /**
     * Builds and returns a unique identifier for this exception instance. This unique id consists of the ip address of
     * the FHIR server host, represented in hex, followed by a UUID.
     * @return String - A unique identifier for this exception instance.
     */
    public String getUniqueId() {

        StringBuffer uniqueIdPrefix = new StringBuffer();
        String localIpAddr = "";
        String[] localIpAddrParts;
        int localIpAddrPart;

        if (uniqueId == null) {

            IPHandler ipHandler = new IPHandler();
            localIpAddr = ipHandler.getIpAddress();
            localIpAddrParts = localIpAddr.split("\\.");
            for (int i = 0; i < localIpAddrParts.length; i++) {
                localIpAddrPart = Integer.parseInt(localIpAddrParts[i]);
                uniqueIdPrefix.append(Integer.toHexString(localIpAddrPart));
                uniqueIdPrefix.append("-");
            }

            uniqueId = uniqueIdPrefix + UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    @Override
    public String toString() {
        String superMsg = super.toString();
        StringBuilder myMsg = new StringBuilder();

        if (superMsg != null && !superMsg.isEmpty()) {
              myMsg.append(superMsg).append("  ");
        }
        myMsg.append("[probeId=").append(this.getUniqueId()).append("]");

        return myMsg.toString();
    }

    /**
     * Add this exception's probeId value to the message
     * @param msg
     * @return
     */
    public String addProbeId(String msg) {
        StringBuilder result = new StringBuilder();
        result.append(msg);
        result.append(" [probeId=").append(this.getUniqueId()).append("]");

        return result.toString();
    }
}
