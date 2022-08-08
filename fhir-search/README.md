# FHIR Search
This readme covers details related to IBM FHIR Server's search implementation. 

## FHIR Search: Positional Searching
In Release 4.0, the IBM FHIR Server updates the support for [FHIR Search: Positional Searching](https://www.hl7.org/fhir/location.html#positional) using the search parameter `near`.  The `near` implementation depends on Location.position extracted from the Location resource.  

The query parameter near, as in `GET [base]/Location?near=-83.694810|42.256500|11.20|km`  follows the pattern `[latitude]|[longitude]|[distance]|[units]`. The query parameter uses a: 
    - latitude - -90 to 90
    - longitude - -180 to 180
    - distance - the radius or projection from the maximum available latitude/longitude
    - units - a unit of US Statue Lengths, Metric units and British Statute Lengths  per the Unit-of-Measure site - case sensitive or case insensitive unit. 
   

The package `org.linuxforhealth.fhir.search.location` contains:
- Unit of Measure - enumerations and lookup manager
- Bounding Area
    - Bounding Radius a circle with a radius of distance using radians
    - Bounding Box a square with a side 2 * distance
- NearLocationHandler - builds the bounding areas based on the input query parameter values
    - the multiple values are converted into multiple bounding areas
    - each unit of measure is converted into kilometers.
- Util - providing handy logic used in the fhir-persistence layer to calculate or check location / near

The Bounding Areas do not wrap over the poles.  The code is setup to support extensions over the poles and expand the various bounding types. The code provides two types of bounding areas to start and can be refined by downstream consumers. 

Note, the Bounding Area calculation has a small error rate, however it is worth noting that at a 2000km radius there is .7% error when using a bounding box. When a smaller box is used, the precision is more accurate the distance between the edges of the boxes. 

A note on the extraction, the `near` parameter extracts `Location.position` into the LAT_LNG Location Values table.