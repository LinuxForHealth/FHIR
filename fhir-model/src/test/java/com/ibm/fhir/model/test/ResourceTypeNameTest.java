package com.ibm.fhir.model.test;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.core.ResourceTypeName;
import com.ibm.fhir.model.type.code.ResourceType;

public class ResourceTypeNameTest {
    @Test
    void testResourceTypeNameEnum() {
        List<String> typename = Arrays.stream(ResourceTypeName.values())
            .map(t -> t.value())
            .collect(Collectors.toList());

        List<String> types = Arrays.stream(ResourceType.Value.values())
                .map(t -> t.value())
                .collect(Collectors.toList());

        assertEquals(typename, types);
    }
}
