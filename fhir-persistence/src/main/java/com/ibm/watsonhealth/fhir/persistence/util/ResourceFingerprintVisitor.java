/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;



import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Base64;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareVisitorAdapter;

/**
 * Compute a cryptographic hash of the visited nodes, skipping those which
 * may be altered by the persistence layer.
 * @author rarnold
 *
 */
public class ResourceFingerprintVisitor extends PathAwareVisitorAdapter {
    
    
    // 32 bytes chosen as a matching entropy of SHA-256
    private static final int BYTES_FOR_256_BITS = 256 / 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    // the salt we use for computing the hash
    private final byte[] salt;
    
    // The current path as we traverse the model
    private String currentResourceName;
    
    private final MessageDigest digest;
    
    // for tracking array elements
    int index;
    
    /**
     * Public constructor. Uses the given salt
     * @param b64Salt
     */
    public ResourceFingerprintVisitor(byte[] salt) {
        this.salt = salt;
        
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
        }
        catch (NoSuchAlgorithmException x) {
            throw new IllegalStateException(x);
        }
    }

    /**
     * Public constructor. Generates a new salt
     * @param b64Salt
     */
    public ResourceFingerprintVisitor() {
        this.salt = new byte[BYTES_FOR_256_BITS];
        RANDOM.nextBytes(salt);
        
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
        }
        catch (NoSuchAlgorithmException x) {
            throw new IllegalStateException(x);
        }
    }
    

    /**
     * Compute the digest and return the result along with the salt that
     * was used
     * @return
     */
    public SaltHash getSaltAndHash() {
        return new SaltHash(salt, digest.digest());
    }
    
    @Override
    protected void doVisitStart(String elementName, Resource resource) {
        this.currentResourceName = resource.getClass().getSimpleName();
    }
    
    @Override
    protected void doVisitStart(String elementName, Element element) {
        // NOP
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        digest.update(getPath().getBytes(StandardCharsets.UTF_8));
        digest.update(value);
    }
    
    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        updateDigest(getPath(), value.toString());
    }
    
    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(value);
        digest.update(bb);
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        updateDigest(getPath(), value.toString());
        
    }
    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        updateDigest(getPath(), value.toString());
        
    }
    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
        // exclude the meta.versionId value from the fingerprint because it
        // is injected by FHIR and therefore not part of the original
        // resource
        String idName = currentResourceName + ".meta.versionId";
        String path = getPath();
        if (!idName.equals(path)) {
            updateDigest(path, value.toString());
        }
        
    }
    @Override
    public void visit(java.lang.String elementName, Year value) {
        updateDigest(getPath(), value.toString());
        
    }
    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        updateDigest(getPath(), value.toString());
        
    }
    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        // Exclude the lastUpdated value from the fingerprint because this value
        // is injected by the FHIR persistence layer
        String lastUpdatedName = currentResourceName + ".meta.lastUpdated";
        String path = getPath();
        if (!lastUpdatedName.equals(path)) {
            updateDigest(path, value.toString());
        }
        
    }
    
    /**
     * Update the digest with the name/value pair
     * @param name
     * @param value
     */
    protected void updateDigest(String name, String value) {
        digest.update(name.getBytes(StandardCharsets.UTF_8));
        digest.update(value.getBytes(StandardCharsets.UTF_8));
    }
}
