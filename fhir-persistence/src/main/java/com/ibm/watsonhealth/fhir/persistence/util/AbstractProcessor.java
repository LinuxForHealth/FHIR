/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.ibm.watsonhealth.fhir.model.Element;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceProcessorException;

public abstract class AbstractProcessor<T> implements Processor<T> {
	@SuppressWarnings("unchecked")
	public T process(SearchParameter parameter, Object value) throws  FHIRPersistenceProcessorException {
		try {
			Class<?> valueType = value.getClass();
			if (isEnumerationWrapper(valueType)) {
				value = getValue(value);
				valueType = String.class;
			} else if (valueType == Extension.class) {
				value = getValue((Extension) value);
				valueType = value.getClass();
			} else if (valueType.getDeclaredFields().length == 0) {
				valueType = valueType.getSuperclass();
			}
			Method processMethod = this.getClass().getMethod("process", SearchParameter.class, valueType);
			return (T) processMethod.invoke(this, parameter, value);
		}
		catch(NoSuchMethodException | IllegalAccessException e) 
		{
			throw new FHIRPersistenceProcessorException(e);
		}
		catch(InvocationTargetException e) {
			Throwable targetException = e.getCause();
			if (targetException instanceof FHIRPersistenceProcessorException) {
				FHIRPersistenceProcessorException spe = (FHIRPersistenceProcessorException) targetException;
				throw spe;
			}
			else {
				throw new FHIRPersistenceProcessorException(targetException);
			}
		}
	}

	protected boolean isEnumerationWrapper(Class<?> valueType) {
		try {
			Field valueField = valueType.getDeclaredField("value");
			if (valueField.getType().isEnum()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	protected String getValue(Object wrapper) {
		try {
			Object literal = wrapper.getClass().getMethod("getValue").invoke(wrapper);
			return (String) literal.getClass().getMethod("value").invoke(literal);
		} catch (Exception e) {
		}
		return null;
	}

	protected Element getValue(Extension extension) {
		try {
			for (Method method : Extension.class.getDeclaredMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("getValue")) {
					Object value = method.invoke(extension);
					if (value != null) {
						return (Element) value;
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	protected Timestamp convertToTimestamp(String str) throws ParseException {
		try {
			return processDate(str, "yyyy-MM-dd'T'hh:mm:ss");
		} catch (ParseException e) {
			try {
				return processDate(str, "yyyy-MM-dd'T'hh:mm");
			} catch (ParseException e1) {
				try {
					return processDate(str, "yyyy-MM-dd");
				} catch (ParseException e2) {
					return new Timestamp(System.currentTimeMillis());
				}
			}
		} 
	}

	private Timestamp processDate(String date, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT 0:00"));
		java.util.Date parsedDate = dateFormat.parse(date);
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
		sdfDate.setTimeZone(java.util.TimeZone.getTimeZone("GMT 0:00"));
		long tlong = parsedDate.getTime();
		long lTime = TimeZone.getDefault().getOffset(tlong);
		return new Timestamp(tlong + lTime);

	}
}
