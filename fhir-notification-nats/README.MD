# fhir-notification-nats

The fhir-notification-nats module is designed to publish patient records to a [NATS](http://nats.io) streaming cluster for pub/sub, so published patient records can be consumed by designated downstream applications.  

## Configure IBM FHIR
In fhir-server/liberty-config/config/default/fhir-server-config.json, find the default NATS configuration shown here:  

```json
    "nats": {
        "enabled": false,
        "cluster": "nats-streaming",
        "channel": "fhirNotifications",
        "clientId": "fhir-server",
        "servers": "nats://nats-node1:4222,nats://nats-node2:4222,nats://nats-node3:4222",
        "useTLS": false,
        "truststoreLocation": "resources/security/nats.client.truststore.jks",
        "truststorePassword": "change-password",
        "keystoreLocation": "resources/security/nats.client.keystore.jks",
        "keystorePassword": "change-password"
    }
```
 
Set "enabled" to true and provide the name of your NATS cluster for the value of "cluster".  You can leave "channel" and "clientId" as defined.  Provide the URL for one or more NATS servers as the value for "servers". See the "Use TLS" section below for a discussion of the TLS settings, if required.  

For use with the test configuration in the next section, set "enabled" to true and make no other changes.

## Create a simple NATS cluster for testing

In the directory fhir-server-test/src/test/resources/nats you will find the files to set up a basic NATS cluster and IBM FHIR Server on the same docker network.  Follow these steps to test:

* Build your IBM FHIR image with the NATS configuration discussed above and publish it to Docker Hub.  Edit the fhir_server service image in fhir-notification-nats/src/test/integration/docker-compose.yml to point to this new image.  


```  
fhir_server:  
	image: <your-repo>/<your-image>  
```  


Note: As an alternative to building and publishing your own image, you can start an IBM FHIR Server image that contains the NATS publisher functionality, then copy the updated configuration file containing the above changes into the IBM FHIR container and restart the container.  
 
```
Example: 
docker cp ./fhir-server-config.json fhir_server:/opt/ol/wlp/usr/servers/fhir-server/config/default/fhir-server-config.json
```        

* Start the NATS cluster and IBM FHIR server.  

```
cd fhir-server-test/src/test/resources/nats
docker-compose up -d
```  

You can view the logs with:  

```
docker-compose logs -f  
```

* Start the NATS subscriber using [Node.js](https://nodejs.org/en/download/).  

```  
npm install node-nats-streaming  
node nats-subscriber  
```  

* POST a patient record to IBM FHIR server and observe the NATS message received by the subscriber in the console output.


## Use TLS (advanced)

To use TLS (which you must do if your NATS cluster requires TLS) set "useTLS" to true in fhir-server-config.json and provide client truststore and keystore locations and passwords as the remaining config values.  Furthermore, to use TLS to connect to a NATS cluster, that NATS cluster must be enabled to use TLS.  The example above does not use TLS.

### Create a truststore and keystore

On MacOS, install the [mkcert tool](https://github.com/FiloSottile/mkcert) with `brew install mkcert`, then follow these steps to create a Java keystore and truststore for use with NATS.  You also need Java keytool and openssl installed. Set change-password to be any value. 

```shell
# Install the CA locally
./mkcert -install 
 
# Create the cert to use for the IBM FHIR Server client connection
./mkcert -cert-file fhir-cert.pem -key-file fhir-key.pem localhost 127.0.0.1. 

# Create the java trust store
keytool -keystore nats.client.truststore.jks -alias CARoot -import -file ./rootCA.pem -storepass change-password -noprompt -storetype pkcs12

# Convert the cert from .pem to pkcs12
cat ./fhir-key.pem ./fhir-cert.pem > combined.pem
openssl pkcs12 -export -in combined.pem -out cert.p12 -passin pass:change-password -passout pass:change-password

# Import the pkcs12 cert into a new keystore
keytool -importkeystore -srckeystore cert.p12 -srcstoretype pkcs12 -deststoretype pkcs12 -destkeystore nats.client.keystore.jks -srcstorepass change-password -deststorepass change-password

# Import the CA cert into the keystore & clean up
keytool -keystore nats.client.keystore.jks -alias CARoot -import -file ./rootCA.pem -storepass change-password -noprompt
rm cert.p12 combined.pem  
```
