package com.ibm.fhir.persistence.jdbc.search.test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.testng.annotations.Test;

import com.ibm.fhir.model.type.DateTime;

public class MyTest {

    @Test
    public void testMy(){
        System.out.println();
        
        Instant instant = Instant.from(DateTime.PARSER_FORMATTER.parse("2019-11-18T23:27:45.003000Z"));
        System.out.println(instant);
        //java.time.Instant.now();
        //2019-11-18 13:37:52.424918
        
        System.out.println(instant.truncatedTo(ChronoUnit.MILLIS));
        System.out.println(instant);
    }
    
    
}
