/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;
import com.ibm.fhir.search.SearchConstants;

/**
 * Collection of helper methods related to the persistence of FHIR resource payload data
 */
public class FHIRPersistenceSupport {
    // the logger to use for this class
    private static final Logger logger = Logger.getLogger(FHIRPersistenceSupport.class.getName());
    
    // initial buffer size for rendered payload
    private static final int DATA_BUFFER_INITIAL_SIZE = 10*1024; // 10KiB

    /**
     * Render the payload
     * @param resource
     * @param compress
     * @return
     */
    public static InputOutputByteStream render(Resource resource, boolean compress) throws FHIRGeneratorException, IOException {
        InputOutputByteStream ioStream = new InputOutputByteStream(DATA_BUFFER_INITIAL_SIZE);
        
        if (compress) {
            try (GZIPOutputStream zipStream = new GZIPOutputStream(ioStream.outputStream())) {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, zipStream);
            } catch (IOException | FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Failed generating resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw x;
            }
        } else {
            // not compressed, so render directly to the ioStream
            try {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, ioStream.outputStream());            
            } catch (FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Failed generating resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw x;
            }
        }
        return ioStream;
    }
    
    /**
     * Parse the given stream, using elements if needed
     * @param <T>
     * @param resourceType
     * @param in
     * @param elements
     * @param uncompress
     * @return
     */
    public static <T extends Resource> T parse(Class<T> resourceType, InputStream in, List<String> elements, boolean uncompress) throws FHIRParserException, IOException {
        T result;
        try {
            if (uncompress) {
                // Wrap the InputStream so we uncompress the content when reading...and
                // see we close the stream as required in the finally block
                in = new GZIPInputStream(in);
            }
            if (elements != null) {
                // parse/filter the resource using elements
                result = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parseAndFilter(in, elements);
                if (resourceType.equals(result.getClass()) && !FHIRUtil.hasTag(result, SearchConstants.SUBSETTED_TAG)) {
                    // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                    result = FHIRUtil.addTag(result, SearchConstants.SUBSETTED_TAG);
                }
            } else {
                result = FHIRParser.parser(Format.JSON).parse(in);
            }
        } finally {
            if (uncompress) {
                // make sure we always close the GZIPInputStream to avoid leaking resources it holds onto
                in.close();
            }
        }

        return result;
    }

    /**
     * Get the current time which can be used for the lastUpdated field
     * @return current time in UTC
     */
    public static com.ibm.fhir.model.type.Instant getCurrentInstant() {
        return com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
    }

    /**
     * Obtain the versionId value from the Resource meta element, converting
     * to an int for use by the persistence layer
     * @param resource
     * @return
     * @throws FHIRPersistenceException
     */
    public static int getMetaVersionId(Resource resource) throws FHIRPersistenceException {
        // Programming error if this is being called before the meta element has been set
        // properly on the resource
        if (resource.getMeta() == null || resource.getMeta().getVersionId() == null) {
            throw new FHIRPersistenceException("Resource missing meta versionId");
        }
        
        String versionIdValue = resource.getMeta().getVersionId().getValue();
        if (versionIdValue == null) {
            throw new FHIRPersistenceException("Resource missing meta versionId value");
        }
        return Integer.parseInt(versionIdValue);
    }
}