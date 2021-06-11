package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.opencds.cqf.cql.engine.runtime.Tuple;

import com.ibm.fhir.cql.engine.converter.FhirTypeConverter;
import com.ibm.fhir.cql.engine.converter.impl.FhirTypeConverterImpl;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;

public class ParameterConverterTest {

    ParameterConverter converter;
    
    @Before
    public void setup() {
        FhirTypeConverter typeConverter = new FhirTypeConverterImpl();
        converter = new ParameterConverter(typeConverter);
    }
    
    @Test
    public void testParameterConversion() throws Exception {
        
        Patient p = TestHelper.john_doe().build();
        
        LinkedHashMap<String,Object> tupleParts = new LinkedHashMap<>();
        tupleParts.put("Part1", "I am a String");
        tupleParts.put("Part2", p);
        
        List<?> resources = Arrays.asList(p,p,p);
        
        Map<String,Object> input = new HashMap<>();
        input.put("SystemString", "I am SystemString");
        input.put("FHIRElement", fhirstring("I am FHIRElement"));
        input.put("Resources", resources);
        input.put("ShouldBeIgnored", new Tuple().withElements(tupleParts));
        
        Parameter result = converter.toParameter("return", input);
        assertNotNull("Null result", result);
        System.out.println(result.toString());
        
        ParameterMap indexByName = new ParameterMap(result.getPart());
        assertFalse("Tuple should be ignored", indexByName.containsKey("ShouldBeIgnored"));
        
        List<Parameter> params = indexByName.getRequiredParameter("Resources");
        assertEquals( resources.size(), params.size() );
        params.forEach( part -> assertNotNull(part.getResource()) );
    }
}
