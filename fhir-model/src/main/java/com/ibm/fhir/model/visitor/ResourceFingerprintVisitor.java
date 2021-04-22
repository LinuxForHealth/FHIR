/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

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

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.SaltHash;

/**
 * Compute a cryptographic hash of the visited nodes, skipping those which
 * may be altered by the persistence layer.
 */
public class ResourceFingerprintVisitor extends PathAwareVisitor {

    // 32 bytes chosen as a matching entropy of SHA-256
    private static final int BYTES_FOR_256_BITS = 256 / 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    // the salt we use for computing the hash
    private final byte[] salt;

    // The name of the resource we first encounter
    private String currentResourceName;

    private final MessageDigest digest;

    // for tracking array elements
    int index;

    /**
     * Public constructor. Uses the given salt
     * @param salt
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
     * Public constructor. Uses the salt from the given SaltHash
     * Preferred, to avoid confusion between salt and hash
     * @param baseline
     */
    public ResourceFingerprintVisitor(SaltHash baseline) {
        this(baseline.getSalt());
    }

    /**
     * Public constructor. Generates a new salt
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
     * Compute the digest and return the result along with the salt that was used
     * @return
     */
    public SaltHash getSaltAndHash() {
        return new SaltHash(salt, digest.digest());
    }

    @Override
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
        if (this.currentResourceName == null) {
            this.currentResourceName = resource.getClass().getSimpleName();
        }
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        if (includePath()) {
            digest.update(getPath().getBytes(StandardCharsets.UTF_8));
            digest.update(value);
        }
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        if (includePath()) {
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(value);
            digest.update(bb);
        }
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }
    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }
    @Override
    public void doVisit(java.lang.String elementName, java.lang.String value) {
        // exclude the id and meta.versionId values from the fingerprint
        // because they are injected by FHIR. NOTE: startsWith is important
        // because we need to ignore any extension fields which may be
        // present
        if (includePath()) {
            updateDigest(getPath(), value);
        }
    }
    @Override
    public void visit(java.lang.String elementName, Year value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }
    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        if (includePath()) {
            updateDigest(getPath(), value.toString());
        }
    }
    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        // Exclude the lastUpdated value from the fingerprint because this value
        // is injected by the FHIR persistence layer
        if (includePath()) {
            updateDigest(getPath(), value.toString());
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

    /**
     * Test whether or not the current path value should be included in the fingerprint
     * @return
     */
    protected boolean includePath() {
        String idName = currentResourceName + ".id";
        String versionIdName = currentResourceName + ".meta.versionId";
        String lastUpdatedName = currentResourceName + ".meta.lastUpdated";
        String path = getPath();
        return !path.startsWith(idName) && !path.startsWith(versionIdName) && !path.startsWith(lastUpdatedName);

    }
}
