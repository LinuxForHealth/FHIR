/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.remote.provider;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_BOOLEAN;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameter;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameters;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getPart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.annotation.Cacheable;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Designation;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Property;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.provider.FHIRJsonProvider;
import com.ibm.fhir.provider.FHIRProvider;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

/**
 * An implementation of the {@link FHIRTermServiceProvider} interface that connects to an external service using a REST client to access code system content.
 *
 * <p>The external service must implement the FHIR REST terminology APIs documented <a href="http://hl7.org/fhir/terminology-service.html">here</a>
 */
public class RemoteTermServiceProvider implements FHIRTermServiceProvider {
    private static final Logger log = Logger.getLogger(RemoteTermServiceProvider.class.getName());

    private final Configuration configuration;
    private final String base;
    private Client client;

    public RemoteTermServiceProvider(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        this.base = configuration.getBase();
        try {
            log.info("Creating client...");

            ClientBuilder cb = ClientBuilder.newBuilder();

            cb.register(new FHIRProvider(RuntimeType.CLIENT));
            cb.register(new FHIRJsonProvider(RuntimeType.CLIENT));

            if (configuration.getBasicAuth() != null) {
                cb.register(new BasicAuthFilter(configuration.getBasicAuth()));
            }

            cb.property("thread.safe.client", true);

            if (configuration.getTrustStore() != null) {
                cb.trustStore(loadKeyStoreFile(configuration.getTrustStore()));
            }

            if (usingSSLTransport() && !configuration.isHostnameVerificationEnabled()) {
                cb.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }

            cb.property("http.receive.timeout", configuration.getHttpTimeout());

            client = cb.build();
        } catch (Exception e) {
            throw new Error("An error occured while creating RemoteTermServiceProvider client", e);
        }
    }

    @Cacheable
    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        return getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.IS_A)
            .value(code)
            .build()));
    }

    @Cacheable
    @Override
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        WebTarget target = client.target(base);

        Response response = target.path("CodeSystem").path("$lookup")
            .queryParam("system", codeSystem.getUrl().getValue())
            .queryParam("code", code.getValue())
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            Parameters parameters = response.readEntity(Parameters.class);
            return toConcept(code, parameters);
        }

        throw errorOccurred(response, "CodeSystem $lookup");
    }

    @Cacheable
    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        return getConcepts(codeSystem, Collections.emptyList());
    }

    @Cacheable
    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        return getConcepts(codeSystem, filters, Function.identity());
    }

    @Cacheable
    @Override
    public <R> Set<R> getConcepts(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        Parameters parameters = buildValueSetExpandParameters(codeSystem, filters);

        WebTarget target = client.target(base);

        Response response = target.path("ValueSet").path("$expand")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .post(Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON));

        if (response.getStatus() == Status.OK.getStatusCode()) {
            JsonObject valueSet = response.readEntity(JsonObject.class);
            JsonObject expansion = valueSet.getJsonObject("expansion");

            Set<R> result = expansion.containsKey("total") ?
                new LinkedHashSet<>(expansion.getInt("total")) :
                new LinkedHashSet<>();

            for (JsonValue contains : expansion.getJsonArray("contains")) {
                result.add(function.apply(toConcept(contains.asJsonObject())));
            }

            return result;
        }

        throw errorOccurred(response, "ValueSet $expand");
    }

    @Cacheable
    @Override
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        WebTarget target = client.target(base);

        Response response = target.path("CodeSystem")
            .path("$validate-code")
            .queryParam("url", codeSystem.getUrl().getValue())
            .queryParam("code", code.getValue())
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            Parameters parameters = response.readEntity(Parameters.class);
            Parameter resultParameter = getParameter(parameters, "result");
            if (resultParameter != null && FHIR_BOOLEAN.isInstance(resultParameter.getValue())) {
                return Boolean.TRUE.equals(resultParameter.getValue().as(FHIR_BOOLEAN).getValue());
            }
        }

        return false;
    }

    @Override
    public boolean isSupported(CodeSystem codeSystem) {
        for (Configuration.Supports supports : configuration.getSupports()) {
            if (supports.getSystem().equals(codeSystem.getUrl().getValue()) &&
                    (supports.getVersion() == null || codeSystem.getVersion() == null ||
                        supports.getVersion().equals(codeSystem.getVersion().getValue()))) {
                return true;
            }
        }
        return false;
    }

    @Cacheable
    @Override
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        WebTarget target = client.target(base);

        Response response = target.path("CodeSystem")
            .path("$subsumes")
            .queryParam("system", codeSystem.getUrl().getValue())
            .queryParam("codeA", codeA.getValue())
            .queryParam("codeB", codeB.getValue())
            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
            .get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            Parameters parameters = response.readEntity(Parameters.class);
            Parameter outcomeParameter = getParameter(parameters, "outcome");
            if (outcomeParameter != null && FHIR_STRING.isInstance(outcomeParameter.getValue())) {
                ConceptSubsumptionOutcome outcome = ConceptSubsumptionOutcome.of(outcomeParameter.getValue().as(FHIR_STRING).getValue());
                return ConceptSubsumptionOutcome.SUBSUMES.equals(outcome) || ConceptSubsumptionOutcome.EQUIVALENT.equals(outcome);
            }
        }

        throw errorOccurred(response, "CodeSystem $subsumes");
    }

    /**
     * Close the client associated with this remote term service provider.
     */
    public void close() {
        log.info("Closing client...");
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
          log.log(Level.SEVERE, "An error occured while closing client", e);
        } finally {
            client = null;
        }
    }

    /**
     * Get the configuration used to create this remote term service provider.
     *
     * @return
     *     the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    private Parameters buildValueSetExpandParameters(CodeSystem codeSystem, List<Filter> filters) {
        return Parameters.builder()
            .parameter(Parameter.builder()
                .name(string("valueSet"))
                .resource(ValueSet.builder()
                    .status(PublicationStatus.ACTIVE)
                    .compose(Compose.builder()
                        .include(Include.builder()
                            .system(codeSystem.getUrl())
                            .version(codeSystem.getVersion())
                            .filter(filters)
                            .build())
                        .build())
                    .build())
                .build())
            .build();
    }

    private FHIRTermServiceException errorOccurred(Response response, String op) {
        OperationOutcome outcome = null;
        try {
            outcome = response.readEntity(OperationOutcome.class);
        } catch (IllegalArgumentException | ProcessingException e) {
            log.log(Level.SEVERE, "An error occurred while reading the entity", e);
        }
        List<Issue> issues = (outcome != null) ? outcome.getIssue() : Collections.emptyList();
        return new FHIRTermServiceException("An error occurred during the " + op + " operation", issues);
    }

    private KeyStore loadKeyStoreFile(Configuration.TrustStore trustStore) {
        return loadKeyStoreFile(trustStore.getLocation(), trustStore.getPassword(), trustStore.getType());
    }

    private KeyStore loadKeyStoreFile(String location, String password, String type) {
        InputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(type);

            URL url = Thread.currentThread().getContextClassLoader().getResource(location);
            if (url != null) {
                in = url.openStream();
            }

            if (in == null) {
                File file = new File(location);
                if (file.exists()) {
                    in = new FileInputStream(file);
                }
            }

            if (in == null) {
                throw new FileNotFoundException("KeyStore file '" + location + "' was not found.");
            }

            keyStore.load(in, password.toCharArray());

            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new IllegalStateException("Error loading keystore file '" + location + "' : " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) { }
            }
        }
    }

    private Concept toConcept(Code code, Parameters parameters) {
        Concept.Builder conceptBuilder = Concept.builder();

        conceptBuilder.code(code);

        Parameter displayParameter = getParameter(parameters, "display");
        if (displayParameter != null) {
            conceptBuilder.display(displayParameter.getValue().as(FHIR_STRING));
        }

        for (Parameter designationParameter : getParameters(parameters, "designation")) {
            Designation.Builder designationBuilder = Designation.builder();

            Parameter languageParameter = getPart(designationParameter, "language");
            if (languageParameter != null && languageParameter.getValue() instanceof Code) {
                designationBuilder.language((Code) languageParameter.getValue());
            }

            Parameter useParameter = getPart(designationParameter, "use");
            if (useParameter != null && useParameter.getValue() instanceof Coding) {
                designationBuilder.use((Coding) useParameter.getValue());
            }

            Parameter valueParameter = getPart(designationParameter, "value");
            if (valueParameter == null || !FHIR_STRING.isInstance(valueParameter.getValue())) {
                log.warning("Required parameter 'value' is null or has an invalid value type");
                continue;
            }
            designationBuilder.value(valueParameter.getValue().as(FHIR_STRING));

            conceptBuilder.designation(designationBuilder.build());
        }

        for (Parameter propertyParameter : getParameters(parameters, "property")) {
            Property.Builder propertyBuilder = Property.builder();

            Parameter codeParameter = getPart(propertyParameter, "code");
            if (codeParameter == null || !FHIR_STRING.isInstance(codeParameter.getValue())) {
                log.warning("Required parameter 'code' is null or has an invalid value type");
                continue;
            }
            propertyBuilder.code(Code.of(codeParameter.getValue().as(FHIR_STRING).getValue()));

            Parameter valueParameter = getPart(propertyParameter, "value");
            if (valueParameter == null) {
                valueParameter = getPart(propertyParameter, "valueString");
            }
            if (valueParameter == null || valueParameter.getValue() == null) {
                log.warning("Required parameter 'value' is null or has an invalid value type");
                continue;
            }
            propertyBuilder.value(valueParameter.getValue());

            conceptBuilder.property(propertyBuilder.build());
        }

        return conceptBuilder.build();
    }

    private Concept toConcept(JsonObject contains) {
        return Concept.builder()
            .code(Code.of(contains.getString("code")))
            .display(string(contains.getString("display")))
            .build();
    }

    private boolean usingSSLTransport() {
        return base.startsWith("https:");
    }

    /**
     * A class used to configure a remote term service provider
     */
    public static class Configuration {
        public static final int DEFAULT_HTTP_TIMEOUT = 60000;
        public static final boolean DEFAULT_HOSTNAME_VERIFICATION_ENABLED = true;

        private final String base;
        private final TrustStore trustStore;
        private final boolean hostnameVerificationEnabled;
        private final BasicAuth basicAuth;
        private final int httpTimeout;
        private final List<Supports> supports;

        private Configuration(Builder builder) {
            base = Objects.requireNonNull(builder.base, "base");
            trustStore = builder.trustStore;
            hostnameVerificationEnabled = builder.hostnameVerificationEnabled;
            basicAuth = builder.basicAuth;
            httpTimeout = builder.httpTimeout;
            supports = Collections.unmodifiableList(builder.supports);
        }

        public String getBase() {
            return base;
        }

        public TrustStore getTrustStore() {
            return trustStore;
        }

        public boolean isHostnameVerificationEnabled() {
            return hostnameVerificationEnabled;
        }

        public BasicAuth getBasicAuth() {
            return basicAuth;
        }

        public int getHttpTimeout() {
            return httpTimeout;
        }

        public List<Supports> getSupports() {
            return supports;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Configuration other = (Configuration) obj;
            return Objects.equals(base, other.base) &&
                    Objects.equals(trustStore, other.trustStore) &&
                    Objects.equals(hostnameVerificationEnabled, other.hostnameVerificationEnabled) &&
                    Objects.equals(basicAuth, other.basicAuth) &&
                    Objects.equals(httpTimeout, other.httpTimeout) &&
                    Objects.equals(supports, other.supports);
        }

        @Override
        public int hashCode() {
            return Objects.hash(base, trustStore, hostnameVerificationEnabled, basicAuth, httpTimeout, supports);
        }

        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String base;
            private TrustStore trustStore;
            private boolean hostnameVerificationEnabled = DEFAULT_HOSTNAME_VERIFICATION_ENABLED;
            private BasicAuth basicAuth;
            private int httpTimeout = DEFAULT_HTTP_TIMEOUT;
            private List<Supports> supports = new ArrayList<>();

            private Builder() { }

            public Builder base(String base) {
                this.base = base;
                return this;
            }

            public Builder trustStore(TrustStore trustStore) {
                this.trustStore = trustStore;
                return this;
            }

            public Builder hostnameVerificationEnabled(boolean hostnameVerificationEnabled) {
                this.hostnameVerificationEnabled = hostnameVerificationEnabled;
                return this;
            }

            public Builder basicAuth(BasicAuth basicAuth) {
                this.basicAuth = basicAuth;
                return this;
            }

            public Builder httpTimeout(int httpTimeout) {
                this.httpTimeout = httpTimeout;
                return this;
            }

            public Builder supports(Supports... supports) {
                for (Supports value : supports) {
                    this.supports.add(value);
                }
                return this;
            }

            public Builder supports(Collection<Supports> supports) {
                this.supports = new ArrayList<>(supports);
                return this;
            }

            public Configuration build() {
                return new Configuration(this);
            }

            protected Builder from(Configuration configuration) {
                base = configuration.base;
                trustStore = configuration.trustStore;
                hostnameVerificationEnabled = configuration.hostnameVerificationEnabled;
                basicAuth = configuration.basicAuth;
                httpTimeout = configuration.httpTimeout;
                supports.addAll(configuration.supports);
                return this;
            }
        }

        /**
         * A class that represents the trust store details used by the REST client
         */
        public static class TrustStore {
            public static final String DEFAULT_TYPE = "pkcs12";

            private String location;
            private String password;
            private String type;

            private TrustStore(Builder builder) {
                this.location = Objects.requireNonNull(builder.location, "location");
                this.password = Objects.requireNonNull(builder.password, "password");
                this.type = Objects.requireNonNull(builder.type, "type");
            }

            public String getLocation() {
                return location;
            }

            public String getPassword() {
                return password;
            }

            public String getType() {
                return type;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                TrustStore other = (TrustStore) obj;
                return Objects.equals(location, other.location) &&
                        Objects.equals(password, other.password) &&
                        Objects.equals(type, other.type);
            }

            @Override
            public int hashCode() {
                return Objects.hash(location, password, type);
            }

            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {
                private String location;
                private String password;
                private String type = DEFAULT_TYPE;

                private Builder() { }

                public Builder location(String location) {
                    this.location = location;
                    return this;
                }

                public Builder password(String password) {
                    this.password = password;
                    return this;
                }

                public Builder type(String type) {
                    this.type = type;
                    return this;
                }

                public TrustStore build() {
                    return new TrustStore(this);
                }

                protected Builder from(TrustStore trustStore) {
                    location = trustStore.location;
                    password = trustStore.password;
                    type = trustStore.type;
                    return this;
                }
            }
        }

        /**
         * A class that represents the basic authentication details used by the REST client
         */
        public static class BasicAuth {
            private final String username;
            private final String password;

            private BasicAuth(Builder builder) {
                username = Objects.requireNonNull(builder.username, "username");
                password = Objects.requireNonNull(builder.password, "password");
            }

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                BasicAuth other = (BasicAuth) obj;
                return Objects.equals(username, other.username) &&
                        Objects.equals(password, other.password);
            }

            @Override
            public int hashCode() {
                return Objects.hash(username, password);
            }

            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {
                private String username;
                private String password;

                private Builder() { }

                public Builder username(String username) {
                    this.username = username;
                    return this;
                }

                public Builder password(String password) {
                    this.password = password;
                    return this;
                }

                public BasicAuth build() {
                    return new BasicAuth(this);
                }

                protected Builder from(BasicAuth basicAuth) {
                    username = basicAuth.username;
                    password = basicAuth.password;
                    return this;
                }
            }
        }

        /**
         * A class that represents the code system(s) supported by a remote term service provider
         */
        public static class Supports {
            private final String system;
            private final String version;

            public Supports(Builder builder) {
                system = Objects.requireNonNull(builder.system, "system");
                version = builder.version;
            }

            public String getSystem() {
                return system;
            }

            public String getVersion() {
                return version;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                Supports other = (Supports) obj;
                return Objects.equals(system, other.system) &&
                        Objects.equals(version, other.version);
            }

            @Override
            public int hashCode() {
                return Objects.hash(system, version);
            }

            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {
                private String system;
                private String version;

                private Builder() { }

                public Builder system(String system) {
                    this.system = system;
                    return this;
                }

                public Builder version(String version) {
                    this.version = version;
                    return this;
                }

                public Supports build() {
                    return new Supports(this);
                }

                protected Builder from(Supports supports) {
                    system = supports.system;
                    version = supports.version;
                    return this;
                }
            }
        }
    }

    private static class BasicAuthFilter implements ClientRequestFilter {
        private final String authHeaderValue;

        public BasicAuthFilter(Configuration.BasicAuth basicAuth) {
            authHeaderValue = buildAuthHeaderValue(basicAuth);
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("Authorization", authHeaderValue);
        }

        private String buildAuthHeaderValue(Configuration.BasicAuth basicAuth) {
            String basicAuthToken = basicAuth.getUsername() + ":" + basicAuth.getPassword();
            return "Basic " + Base64.getEncoder().encodeToString(basicAuthToken.getBytes());
        }
    }
}
