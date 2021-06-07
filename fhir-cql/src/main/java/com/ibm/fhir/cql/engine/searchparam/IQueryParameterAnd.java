package com.ibm.fhir.cql.engine.searchparam;

import java.util.List;

public interface IQueryParameterAnd<T extends IQueryParameterOr<? extends IQueryParameter>> {

    List<T> getParameterValues();
}
