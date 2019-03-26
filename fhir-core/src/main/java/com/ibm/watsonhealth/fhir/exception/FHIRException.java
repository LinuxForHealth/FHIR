/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.exception;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common FHIR Server exception base class.
 */
public class FHIRException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final String CLASSNAME = FHIRException.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);
    
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
            try {
                localIpAddr = Inet4Address.getLocalHost().getHostAddress();
                localIpAddrParts = localIpAddr.split("\\.");
                for (int i = 0; i < localIpAddrParts.length; i++) {
                    localIpAddrPart = Integer.parseInt(localIpAddrParts[i]);
                    uniqueIdPrefix.append(Integer.toHexString(localIpAddrPart));    
                    uniqueIdPrefix.append("-");  
                }
            } 
            catch (UnknownHostException e) {
                log.log(Level.SEVERE, "Failure acquiring local host IP address", e);
            }
            catch(NumberFormatException e) {
                log.log(Level.SEVERE, "Failure parsing local host IP address " + localIpAddr, e);
            }
            
            uniqueId = uniqueIdPrefix + UUID.randomUUID().toString();
        }
        return uniqueId;
    }
    
    @Override
    public String getMessage() {
        String superMsg = super.getMessage();
        StringBuilder myMsg = new StringBuilder();
                
        if (superMsg != null && !superMsg.isEmpty()) {
              myMsg.append(superMsg).append("  ");
        }
         myMsg.append("[probeId=").append(this.getUniqueId()).append("]");
        
        return myMsg.toString();
    }
    
}
