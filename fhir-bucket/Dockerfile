FROM xxxxx

COPY synthea-2.4.0-shaded.jar .
COPY target/synthea-uploader-0.0.1-SNAPSHOT-shaded.jar .
COPY cloudflare.truststore .

RUN echo "x.x.x.x   fhir-service" >> /etc/hosts

ENV LANG en_US.UTF-8

RUN sed -i 's/override_install_langs=en_US.utf8/#override_install_langs=en_US.utf8/g' /etc/yum.conf

CMD java -jar synthea-2.4.0-shaded.jar -p 1000 -o false && java -Djavax.net.ssl.trustStore=clientTruststore.jks -jar synthea-uploader-0.0.1-SNAPSHOT-shaded.jar output/fhir_dstu2/
