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
        "truststoreLocation": "resources/security/nats.client.truststore.p12",
        "truststorePassword": "change-password",
        "keystoreLocation": "resources/security/nats.client.keystore.p12",
        "keystorePassword": "change-password"
    }
```
 
Set "enabled" to true and provide the name of your NATS cluster for the value of "cluster".  You can leave "channel" and "clientId" as defined.  Provide the URL for one or more NATS servers as the value for "servers". See the "Use TLS" section below for a discussion of the TLS settings, if required.  

## Test NATS Notifications
Manually invoke fhir-server-test/src/test/nodejs/nats-subscriber.js via `node nats-subscriber` and POST or PUT a patient record to the IBM FHIR Server to test the NATS notification module.

## Use TLS (Advanced)

To use TLS (which you must do if your NATS streaming cluster requires TLS) set "useTLS" to true in fhir-server-config.json and provide client truststore and keystore locations and passwords as the remaining config values.  Furthermore, to use TLS to connect to a NATS cluster, that NATS cluster must be enabled to use TLS.  The example above does not use TLS.

### Create a truststore and keystore

On MacOS, install the [mkcert tool](https://github.com/FiloSottile/mkcert) with `brew install mkcert`, then follow these steps to create a Java keystore and truststore for use with NATS.  You also need Java keytool and openssl installed. Set change-password to be any value. 

```shell
# Install the CA locally
./mkcert -install 
 
# Create the cert to use for the IBM FHIR Server client connection
./mkcert -cert-file fhir-cert.pem -key-file fhir-key.pem localhost 127.0.0.1. 

# Create the java trust store
keytool -keystore nats.client.truststore.p12 -alias CARoot -import -file ./rootCA.pem -storepass change-password -noprompt -storetype pkcs12

# Convert the cert from .pem to pkcs12
cat ./fhir-key.pem ./fhir-cert.pem > combined.pem
openssl pkcs12 -export -in combined.pem -out cert.p12 -passin pass:change-password -passout pass:change-password

# Import the pkcs12 cert into a new keystore
keytool -importkeystore -srckeystore cert.p12 -srcstoretype pkcs12 -deststoretype pkcs12 -destkeystore nats.client.keystore.p12 -srcstorepass change-password -deststorepass change-password

# Import the CA cert into the keystore & clean up
keytool -keystore nats.client.keystore.p12 -alias CARoot -import -file ./rootCA.pem -storepass change-password -noprompt
rm cert.p12 combined.pem  
```
