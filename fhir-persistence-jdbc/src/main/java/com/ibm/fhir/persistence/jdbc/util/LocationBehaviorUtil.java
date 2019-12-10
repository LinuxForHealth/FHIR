package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LATITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LONGITUDE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.util.QuerySegmentAggregator.PARAMETER_TABLE_ALIAS;

import java.util.List;

import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
import com.ibm.fhir.search.location.bounding.Bounding;

public class LocationBehaviorUtil {
    
    public LocationBehaviorUtil() {
        // No Operation
    }

    
    public void buildLocationSearchQuery(StringBuilder whereClauseSegment, List<Object> bindVariables, List<Bounding> boundingAreas){

        // Now build the piece that compares the BoundingBox longitude and latitude values
        // to the persisted longitude and latitude parameters.
        whereClauseSegment.append(JDBCOperator.AND.value()).append(LEFT_PAREN).append(PARAMETER_TABLE_ALIAS
                + DOT).append(LONGITUDE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                        + DOT).append(LONGITUDE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                                + DOT).append(LATITUDE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR).append(JDBCOperator.AND.value()).append(PARAMETER_TABLE_ALIAS
                                        + DOT).append(LATITUDE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR).append(RIGHT_PAREN).append(RIGHT_PAREN);
        for(Bounding area : boundingAreas) {
            //bindVariables.add(boundingBox.getMaxLongitude());
            //bindVariables.add(boundingBox.getMinLongitude());
            //bindVariables.add(boundingBox.getMaxLatitude());
            //bindVariables.add(boundingBox.getMinLatitude());
            System.out.println(area);
        }
        
    }
    
}
