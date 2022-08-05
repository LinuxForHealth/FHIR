/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import org.linuxforhealth.fhir.exception.FHIRException;

/**
 * Representation of the CADF Geolocation type. Geolocation information, which
 * reveals a resource’s physical location, is obtained by using tracking
 * technologies such as global positioning system (GPS) devices, or IP
 * geolocation by using databases that map IP addresses to geographic locations.
 * Geolocation information is widely used in context-sensitive content delivery,
 * enforcing location-based access restrictions on services, and fraud detection
 * and prevention. Due to the intense concerns about security and privacy,
 * countries and regions introduced various legislation and regulation. To
 * determine whether an event is compliant sometimes depends on the geolocation
 * of the event. Therefore, it is crucial to report geolocation information
 * unambiguously in an audit trail.
 */
public final class CadfGeolocation {
    private final String id;
    private final String latitude;
    private final String longitude;
    private final Double elevation;
    private final Double accuracy;
    private final String city;
    private final String state;
    private final String regionICANN;
    private final ArrayList<CadfMapItem> annotations;

    private CadfGeolocation(Builder builder) {
        this.id          = builder.id;
        this.latitude    = builder.latitude;
        this.longitude   = builder.longitude;
        this.elevation   = builder.elevation;
        this.accuracy    = builder.accuracy;
        this.city        = builder.city;
        this.state       = builder.state;
        this.regionICANN = builder.regionICANN;
        this.annotations = builder.annotations;
    }

    /**
     * Validate contents of the geolocation type.
     * The logic is determined by the CADF specification. In short, either
     * longitude/latitude or city and region must be present.
     *
     * @throws IllegalStateException when the properties do not meet the
     *                               specification.
     */
    private void validate() throws IllegalStateException {
        if (arePresent(this.latitude, this.longitude) || arePresent(this.city, this.regionICANN)) {
            return;
        } else {
            throw new IllegalStateException("missing required location information");
        }
    }

    private boolean isPresent(String s) {
        return s != null && !s.isEmpty();
    }

    private boolean arePresent(String s1, String s2) {
        return isPresent(s1) && isPresent(s2);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the elevation
     */
    public Double getElevation() {
        return elevation;
    }

    /**
     * @return the accuracy
     */
    public Double getAccuracy() {
        return accuracy;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @return the annotations
     */
    public ArrayList<CadfMapItem> getAnnotations() {
        return annotations;
    }

    public String getCity() {
        return city;
    }

    public String getRegionICANN() {
        return regionICANN;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return latitude;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Operation
        }

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(CadfGeolocation obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();
                    generate(obj, generator);
                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }

        public static void generate(CadfGeolocation obj, JsonGenerator generator) throws IOException {

            if (obj.getAccuracy() != null) {
                generator.write("accuracy", obj.getAccuracy());
            }

            if (obj.getElevation() != null) {
                generator.write("elevation", obj.getElevation());
            }

            if (obj.getCity() != null) {
                generator.write("city", obj.getCity());
            }

            if (obj.getState() != null) {
                generator.write("state", obj.getState());
            }

            if (obj.getId() != null) {
                generator.write("id", obj.getId());
            }

            if (obj.getLatitude() != null) {
                generator.write("latitude", obj.getLatitude());
            }

            if (obj.getLongitude() != null) {
                generator.write("longitude", obj.getLongitude());
            }

            if (obj.getRegionICANN() != null) {
                generator.write("region", obj.getRegionICANN());
            }

            //Annotations
            if (obj.getAnnotations() != null) {
                generator.writeStartArray("annotations");
                for (CadfMapItem item : obj.getAnnotations()) {
                    CadfMapItem.Writer.generate(item, generator);
                }
                generator.writeEnd();
            }
        }

    }

    /**
     * Parser
     */
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static CadfGeolocation parse(JsonObject jsonObject)
                throws FHIRException, IOException {
            CadfGeolocation.Builder builder =
                    CadfGeolocation.builder();

            if (jsonObject.get("id") != null) {
                String id = jsonObject.getString("id");
                builder.id(id);
            }

            if (jsonObject.get("latitude") != null) {
                String latitude = jsonObject.getString("latitude");
                builder.latitude(latitude);
            }

            if (jsonObject.get("longitude") != null) {
                String longitude = jsonObject.getString("longitude");
                builder.longitude(longitude);
            }

            if (jsonObject.get("elevation") != null) {
                BigDecimal elevation = jsonObject.getJsonNumber("elevation").bigDecimalValue();
                builder.elevation(elevation.doubleValue());
            }

            if (jsonObject.get("accuracy") != null) {
                BigDecimal accuracy = jsonObject.getJsonNumber("accuracy").bigDecimalValue();
                builder.accuracy(accuracy.doubleValue());
            }

            if (jsonObject.get("city") != null) {
                String city = jsonObject.getString("city");
                builder.city(city);
            }

            if (jsonObject.get("state") != null) {
                String state = jsonObject.getString("state");
                builder.state(state);
            }

            if (jsonObject.get("region") != null) {
                String region = jsonObject.getString("region");
                builder.region(region);
            }

            if (jsonObject.get("annotations") != null) {
                JsonArray annotations = jsonObject.getJsonArray("annotations");
                for (int i = 0; i < annotations.size(); i++) {
                    JsonObject obj = (JsonObject) annotations.get(0);
                    CadfMapItem mapItem = CadfMapItem.Parser.parse(obj);
                    builder.addAnnotation(mapItem);
                }
            }

            return builder.build();
        }

        public static CadfGeolocation parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the CadfGeoLocation", e);
            }
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class Builder {

        private String id;
        private String latitude;
        private String longitude;
        private Double elevation;
        private Double accuracy;
        private String city;
        private String state;
        private String regionICANN;
        private ArrayList<CadfMapItem> annotations = new ArrayList<>();

        /**
         * Geolocation builder using latitude/longitude values.
         *
         * @param latitude  -- String. Latitude values adhere to the format based on ISO
         *                  6709:2008 Annex H.3.1 – H.3.3. [ISO-6709-2008]
         * @param longitude -- String. Longitude values adhere to the format based on
         *                  ISO 6709:2008 Annex H.3.1 – H.3.3. [ISO-6709-2008]
         * @param elevation -- Double. Elevation in meters.
         * @param accuracy  -- Double. Accuracy of geolocation, in meters.
         */
        public Builder(String latitude, String longitude, Double elevation, Double accuracy) {
            this.latitude  = latitude;
            this.longitude = longitude;
            this.elevation = elevation;
            this.accuracy  = accuracy;
        }

        private Builder() {
            // Intentionally hiding from external callers.
        }

        /**
         * Geolocation builder using city/state/region.
         *
         * @param city        - String. Location city.
         * @param state       - String. Location state or province, optional.
         * @param regionICANN - String. Location region -- ICANN country code per top
         *                    level domain (ccTLD) naming convention [IANA-ccTLD]. May
         *                    be upper- or lowercase.
         * @param accuracy    -- Double. Accuracy of geolocation, in meters.
         */
        public Builder(String city, String state, String regionICANN, Double accuracy) {
            this.city        = city;
            this.state       = state;
            this.regionICANN = regionICANN;
            this.accuracy    = accuracy;
        }

        /**
         * Optionally add ICANN regioin data to the location created using
         * latitude/longitude values.
         *
         * @param regionICANN - String. ICANN country code per top level domain (ccTLD)
         *                    naming convention [IANA-ccTLD]. May be upper- or
         *                    lowercase.
         * @return Builder
         */
        public Builder region(String regionICANN) {
            this.regionICANN = regionICANN;
            return this;
        }

        /**
         * Optionally set the location identifier.
         *
         * @param id - String. URI of the location.
         * @return This builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Optionally set arbitrary annotations for this location
         *
         * @param annotations An array of key-value annotations
         * @return This builder
         */
        public Builder annotations(CadfMapItem[] annotations) {
            this.annotations = new ArrayList<>(Arrays.asList(annotations));
            return this;
        }

        /**
         * Optionally set arbitrary annotations for this location
         *
         * @param annotations An array of key-value annotations
         * @return This builder
         */
        public Builder annotations(ArrayList<CadfMapItem> annotations) {
            this.annotations = annotations;
            return this;
        }

        public Builder addAnnotation(CadfMapItem annotation) {
            this.annotations.add(annotation);
            return this;
        }

        public Builder latitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder elevation(Double elevation) {
            this.elevation = elevation;
            return this;
        }

        public Builder accuracy(Double accuracy) {
            this.accuracy = accuracy;
            return this;
        }

        /**
         * Build an immutable geo-location object
         *
         * @return CadfGeolocation
         * @throws IllegalStateException when the properties do not meet the
         *                               specification.
         */
        public CadfGeolocation build() throws IllegalStateException {
            CadfGeolocation loc = new CadfGeolocation(this);
            loc.validate();
            return loc;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}