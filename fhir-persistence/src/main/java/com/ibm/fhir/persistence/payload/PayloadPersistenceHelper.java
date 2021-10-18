/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PayloadPersistenceHelper {
    // the logger to use for this class
    private static final Logger logger = Logger.getLogger(PayloadPersistenceHelper.class.getName());
    
    // initial buffer size for rendered payload
    private static final int DATA_BUFFER_INITIAL_SIZE = 10*1024; // 10KiB

    /**
     * Render the payload
     * @param resource
     * @param compress
     * @return
     */
    public static InputOutputByteStream render(Resource resource, boolean compress) throws FHIRPersistenceException {
        InputOutputByteStream ioStream = new InputOutputByteStream(DATA_BUFFER_INITIAL_SIZE);
        
        if (compress) {
            try (GZIPOutputStream zipStream = new GZIPOutputStream(ioStream.outputStream())) {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, zipStream);
                zipStream.close();
            } catch (IOException | FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw new FHIRPersistenceException("Store payload failed");
            }
        } else {
            // not compressed, so render directly to the ioStream
            try {
                FHIRGenerator.generator(Format.JSON, false).generate(resource, ioStream.outputStream());            
            } catch (FHIRGeneratorException x) {
                logger.log(Level.SEVERE, "Resource: '" + resource.getClass().getSimpleName() + "/" + resource.getId() + "'", x);
                throw new FHIRPersistenceException("Store payload failed");
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
     * @return
     */
    public static <T extends Resource> T parse(Class<T> resourceType, InputStream in, List<String> elements) {
        T result;

        try {
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
        } catch (FHIRParserException x) {
            // need to wrap because this method is being called as a lambda
            throw new RuntimeException(x);
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
}