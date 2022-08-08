/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.visitor;

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.SaltHash;

/**
 * Compute a cryptographic hash of the visited nodes, skipping those which
 * may be altered by the persistence layer.
 */
public class ResourceFingerprintVisitor extends PathAwareVisitor {

    // 32 bytes chosen as a matching entropy of SHA-256
    private static final int BYTES_FOR_256_BITS = 256 / 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    // The salt we use for computing the hash
    private final byte[] salt;

    // Paths to exclude from the fingerprint
    private Set<String> excludePaths;

    private final MessageDigest digest;

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
        if (excludePaths == null) {
            String currentResourceName = resource.getClass().getSimpleName();
            excludePaths = new HashSet<>(Arrays.asList(
                currentResourceName + ".id",
                currentResourceName + ".meta.versionId",
                currentResourceName + ".meta.lastUpdated")
            );
        }
    }

    @Override
    public boolean visit(java.lang.String elementName, int index, org.linuxforhealth.fhir.model.type.String value) {
        // Exclude meta.versionId from the fingerprint because it gets injected by the FHIR server.
        return includePath();
    }

    @Override
    public boolean visit(java.lang.String elementName, int index, org.linuxforhealth.fhir.model.type.Instant value) {
        // Exclude meta.lastUpdated from the fingerprint because it gets injected by the FHIR server.
        return includePath();
    }

    @Override
    public void doVisit(java.lang.String elementName, byte[] value) {
        digest.update(getPath().getBytes(StandardCharsets.UTF_8));
        digest.update(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, BigDecimal value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Boolean value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Integer value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(value);
        bb.flip();
        digest.update(bb);
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalDate value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalTime value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.String value) {
        // exclude the Resource.id from the fingerprint because it is injected by the FHIR server
        if (includePath()) {
            updateDigest(getPath(), value);
        }
    }

    @Override
    public void doVisit(java.lang.String elementName, Year value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, YearMonth value) {
        updateDigest(getPath(), value.toString());
    }

    @Override
    public void doVisit(java.lang.String elementName, ZonedDateTime value) {
        updateDigest(getPath(), value.toString());
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
        return excludePaths == null ? true : !excludePaths.contains(getPath());
    }
}
