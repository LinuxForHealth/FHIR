/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

public class FHIRProvider implements MessageBodyReader<Resource>, MessageBodyWriter<Resource> {
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return supportsMediaType(mediaType);
	}
	
	@Override
	public Resource readFrom(Class<Resource> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		try {
			return FHIRUtil.read(type, getFormat(mediaType), entityStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return supportsMediaType(mediaType);
	}

	@Override
	public void writeTo(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try {
			FHIRUtil.write(t, getFormat(mediaType), entityStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getSize(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	private boolean supportsMediaType(MediaType mediaType) {
		return getFormat(mediaType) != null;
	}

	private Format getFormat(MediaType mediaType) {
		if (mediaType != null) {
			if (mediaType.getSubtype().startsWith(com.ibm.watsonhealth.fhir.core.MediaType.SUBTYPE_JSON_FHIR)) {
				return Format.JSON;
			} else if (mediaType.getSubtype().startsWith(com.ibm.watsonhealth.fhir.core.MediaType.SUBTYPE_XML_FHIR)) {
				return Format.XML;
			}
		}
		return null;
	}
}
