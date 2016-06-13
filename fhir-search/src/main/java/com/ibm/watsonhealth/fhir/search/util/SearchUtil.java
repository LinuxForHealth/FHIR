/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Modifier;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;
import com.ibm.watsonhealth.fhir.search.ParameterValue;
import com.ibm.watsonhealth.fhir.search.ParameterValue.Prefix;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.context.impl.FHIRSearchContextImpl;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;

public class SearchUtil {
    private static final Logger log = Logger.getLogger(SearchUtil.class.getName());
    
    private static final Map<String, Map<String, SearchParameter>> searchParameterMap = buildSearchParameterMap();
	private static final XPath xpath = createXPath();
	private static final Map<String, XPathExpression> expressionMap = new HashMap<String, XPathExpression>();
	
	private static final List<String> SEARCH_RESULT_PARAMETER_NAMES = Arrays.asList("_sort", "_count", "_include", "_revinclude", "_summary", "_elements", "_contained", "_containedType", "_page");
	
	private SearchUtil() { }
	
	public static void init() {
	    // allows us to initialize this class during startup
	}
	
	private static XPath createXPath() {
		XPathFactory xf = XPathFactory.newInstance();
		XPath xp = xf.newXPath();
		xp.setNamespaceContext(new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				if ("f".equals(prefix)) {
					return "http://hl7.org/fhir";
				}
				return null;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				if ("http://hl7.org/fhir".equals(namespaceURI)) {
					return "f";
				}
				return null;
			}

			@Override
			public Iterator<String> getPrefixes(String namespaceURI) {
				return null;
			}
		});
		return xp;
	}
	
	private static XPathExpression getExpression(String expr) throws XPathExpressionException {
		XPathExpression expression = expressionMap.get(expr);
		if (expression == null) {
			// double-checked locking pattern
			synchronized (expressionMap) {
				expression = expressionMap.get(expr);
				if (expression == null) {
					expression = xpath.compile(expr);
					expressionMap.put(expr, expression);
				}
			}
		}
		return expression;
	}
	
	private static Map<String, Map<String, SearchParameter>> buildSearchParameterMap() {
		Map<String, Map<String, SearchParameter>> searchParameterMap = new HashMap<String, Map<String, SearchParameter>>();
		try {
            populateSearchParameterMap(searchParameterMap, "search-parameters.xml");
		} catch (JAXBException e) {
			throw new Error(e);
		}
        try {
            populateSearchParameterMap(searchParameterMap, "extension-search-parameters.xml");
        } catch (Exception e) {
            log.fine("Unable to load extension search parameters from location: ${server.config.dir}/config/extension-search-parameters.xml");
            log.fine(e.getMessage());
        }
		return searchParameterMap;
	}
	
	private static void populateSearchParameterMap(Map<String, Map<String, SearchParameter>> searchParameterMap, String searchParameterFile) throws JAXBException {
        InputStream stream = SearchUtil.class.getClassLoader().getResourceAsStream(searchParameterFile);
        Bundle bundle = FHIRUtil.read(Bundle.class, Format.XML, stream);
        for (BundleEntry entry : bundle.getEntry()) {
            ResourceContainer container = entry.getResource();
            SearchParameter parameter = container.getSearchParameter();
            if (parameter != null) {
                if (!FHIRUtil.isStandardResourceType(parameter.getBase().getValue())) {
                    transformParameter(parameter);
                }
                String base = parameter.getBase().getValue();
                Map<String, SearchParameter> map = searchParameterMap.get(base);
                if (map == null) {
                    map = new TreeMap<String, SearchParameter>();
                    searchParameterMap.put(base, map);
                }
                String name = parameter.getName().getValue();
                map.put(name, parameter);
            }
        }
    }
	
	private static void transformParameter(SearchParameter parameter) {
	    parameter.getBase().setValue("Basic");
	    String path = parameter.getXpath().getValue();
	    parameter.getXpath().setValue(transformPath(path));
    }

    private static String transformPath(String path) {
	    StringBuffer result = new StringBuffer();
	    result.append("f:Basic/");
        path = path.substring(path.indexOf("/") + 1);
        for (String component : path.split("/f:")) {
            String fieldName = component.replace("f:", "");
            result.append("f:extension[@url='http://ibm.com/watsonhealth/fhir/extension/" + fieldName + "']");
            result.append("//");
        }
        int index = result.lastIndexOf("//");
        if (index != -1) {
            result.replace(index, index + 2, "");
        }
        return result.toString();
    }

    public static List<SearchParameter> getSearchParameters(Class<? extends Resource> resourceType) {
		Map<String, SearchParameter> map = searchParameterMap.get(resourceType.getSimpleName());
		if (map != null) {
			return new ArrayList<SearchParameter>(map.values());
		}
		return Collections.emptyList();
	}
	
	public static SearchParameter getSearchParameter(Class<? extends Resource> resourceType, String name) {
		Map<String, SearchParameter> map = searchParameterMap.get(resourceType.getSimpleName());
		if (map != null) {
			return map.get(name);
		}
		return null;
	}
	
	public static <T extends Resource> Map<SearchParameter, List<Object>> extractParameterValues(T resource) throws JAXBException, XPathExpressionException {
		Map<SearchParameter, List<Object>> result = new TreeMap<SearchParameter, List<Object>>(new Comparator<SearchParameter>() {
			@Override
			public int compare(SearchParameter first, SearchParameter second) {
				return first.getName().getValue().compareTo(second.getName().getValue());
			}
		});
		
		Class<? extends Resource> resourceType = resource.getClass();
		
		Binder<Node> binder = FHIRUtil.createBinder(resource);
		Document document = binder.getXMLNode(resource).getOwnerDocument();
		
		List<SearchParameter> parameters = new ArrayList<SearchParameter>(getSearchParameters(Resource.class));
		parameters.addAll(getSearchParameters(resourceType));
		
		for (SearchParameter parameter : parameters) {
			if (parameter.getXpath() != null) {
				String xpath = parameter.getXpath().getValue();
				if (xpath.startsWith("f:Resource")) {
					xpath = xpath.replaceFirst("f:Resource", "f:" + resource.getClass().getSimpleName());
				}
				for (String s : xpath.split("\\|")) {
					XPathExpression expr = getExpression(s);
					NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
					if (nodeList.getLength() > 0) {
						List<Object> values = result.get(parameter);
						if (values == null) {
							values = new ArrayList<Object>();
							result.put(parameter, values);
						}
						for (int i = 0; i < nodeList.getLength(); i++) {
							values.add(binder.getJAXBNode(nodeList.item(i)));
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public static FHIRSearchContext parseQueryParameters(Class<? extends Resource> resourceType, Map<String, List<String>> queryParameters) throws FHIRSearchException {
		FHIRSearchContext context = new FHIRSearchContextImpl();
	    List<Parameter> parameters = new ArrayList<Parameter>();
	    
		for (String name : queryParameters.keySet()) {
		    try {
	            if (isFormatParameter(name) || isSearchResultParameter(name)) {
	                if (isSearchResultParameter(name)) {
	                    parseSearchResultParameter(context, name, queryParameters.get(name));
	                }
	                continue;
	            }
	            
	            // parse name into parameter name
	            String parameterName = name;
	            Modifier modifier = null;
	            String modifierResourceTypeName = null;
	            if (parameterName.contains(":")) {
	                String mod = parameterName.substring(parameterName.indexOf(":") + 1);
	                if (FHIRUtil.isStandardResourceType(mod)) {
	                    modifier = Modifier.TYPE;
	                    modifierResourceTypeName = mod;
	                } else {
	                    modifier = Modifier.fromValue(mod);
	                }
	                parameterName = parameterName.substring(0, parameterName.indexOf(":"));
	            }
	            
	            // get the definition for this search parameter based on resource type and parameter name
	            SearchParameter searchParameter = parameterName.startsWith("_") ? 
	                    getSearchParameter(Resource.class, parameterName) : 
	                    getSearchParameter(resourceType, parameterName);
	            if (searchParameter == null) {
	                throw new FHIRSearchException("Search parameter '" + parameterName + "' for resource type '" + resourceType.getSimpleName() + "' was not found.");
	            }
	                    
	            // get the type of parameter so that we can use it to parse the value
	            Type type = Type.fromValue(searchParameter.getType().getValue());
	            
	            // parse values
	            for (String value : queryParameters.get(name)) {
	                Parameter parameter = new Parameter(type, parameterName, modifier, modifierResourceTypeName);
	                parameters.add(parameter);
	                for (String v : value.split(",")) {
	                    ParameterValue parameterValue = new ParameterValue();
	                    Prefix prefix = null;
	                    switch (type) {
	                    case DATE: {
	                        // date
	                        // [parameter]=[prefix][value]
	                        prefix = getPrefix(v);
	                        if (prefix != null) {
	                            v = v.substring(2);
	                            parameterValue.setPrefix(prefix);
	                        }
	                        parameterValue.setValueDate(FHIRUtilities.parseDateTime(v, false));
	                        break;
	                    }
	                    case NUMBER: {
	                        // number
	                        // [parameter]=[prefix][value]
	                        prefix = getPrefix(v);
	                        if (prefix != null) {
	                            v = v.substring(2);
	                            parameterValue.setPrefix(prefix);
	                        }
	                        parameterValue.setValueNumber(Double.parseDouble(v));
	                        break;
	                    }
	                    case REFERENCE: {
	                        // reference
	                        // [parameter]=[url]
	                        // [parameter]=[type]/[id]
	                        // [parameter]=[id]
	                        parameterValue.setValueString(v);
	                        break;
	                    }
	                    case QUANTITY: {
	                        // quantity
	                        // [parameter]=[prefix][number]|[system]|[code]
	                        prefix = getPrefix(v);
	                        if (prefix != null) {
	                            v = v.substring(2);
	                            parameterValue.setPrefix(prefix);
	                        }
	                        String[] parts = v.split("\\|");
	                        String number = parts[0];
	                        parameterValue.setValueNumber(Double.parseDouble(number));
	                        String system = parts[1];   // could be empty string
	                        parameterValue.setValueSystem(system);
                            String code = parts[2];
                            parameterValue.setValueCode(code);
	                        break;
	                    }
	                    case STRING: {
	                        // string
	                        // [parameter]=[value]
	                        parameterValue.setValueString(v);
	                        break;
	                    }
	                    case TOKEN: {
	                        // token
	                        // [parameter]=[system]|[code]
	                        String[] parts = v.split("\\|");
	                        if (parts.length == 2) {
	                            parameterValue.setValueSystem(parts[0]);
	                            parameterValue.setValueCode(parts[1]);
	                        } else {
	                            parameterValue.setValueCode(v);
	                        }
	                        break;
	                    }
	                    case URI: {
	                        // [parameter]=[value]
	                        parameterValue.setValueString(v);
	                        break;
	                    }
	                    default:
	                        break;
	                    }
	                    parameter.getValues().add(parameterValue);
	                }
	            }
		    } catch (FHIRSearchException e) {
		        throw e;
		    } catch (Exception e) {
		        log.fine("Unable to parse query parameter named: " + name);
		        throw new FHIRSearchException("Unable to parse query parameter named: '" + name + "'", e);
		    }
		}
		
		context.setSearchParameters(parameters);
//      return new FHIRSearchContextImpl(parameters);
		return context;
	}
	
	private static Prefix getPrefix(String s) {
		for (Prefix prefix : Prefix.values()) {
			if (s.startsWith(prefix.value())) {
				return prefix;
			}
		}
		return null;
	}
	
	private static boolean isFormatParameter(String name) {
	    return "_format".equals(name);
	}
	
	private static boolean isSearchResultParameter(String name) {
	    return SEARCH_RESULT_PARAMETER_NAMES.contains(name);
	}
	
	private static void parseSearchResultParameter(FHIRSearchContext context, String name, List<String> values) throws FHIRSearchException {
	    try {
	        String first = values.get(0);
	        if ("_count".equals(name)) {
	            int pageSize = Integer.parseInt(first);
	            context.setPageSize(pageSize);
	        } else if ("_page".equals(name)) {
	            int pageNumber = Integer.parseInt(first);
	            context.setPageNumber(pageNumber);
	        }
	    } catch (Exception e) {
            throw new FHIRSearchException("Unable to parse search result parameter named: '" + name + "'", e);
	    }
	}
}
