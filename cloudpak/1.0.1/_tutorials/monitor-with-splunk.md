---
title: "Monitoring cluster health with Splunk"
description: "Monitor the health of your cluster by using Splunk to capture Kafka broker JMX metrics."
permalink: /tutorials/monitor-with-splunk/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

You can configure {{site.data.reuse.short_name}} to allow JMX scrapers to export Kafka broker JMX metrics to external applications. This tutorial  details how to deploy [jmxtrans](https://github.com/jmxtrans/jmxtrans/){:target="_blank"} into your {{site.data.reuse.icp}} cluster to export Kafka JMX metrics as graphite output to an external Splunk system using a TCP data input.

## Prequisites

- Ensure you have an {{site.data.reuse.short_name}} installation available. This tutorial is based on {{site.data.reuse.short_name}} version 2019.1.1.
- When installing {{site.data.reuse.short_name}}, ensure you select the **Enable secure JMX connections** check box in the [**Kafka broker settings**](../../2019.1.1/installing/configuring/#kafka-broker-settings). This is required to ensure that each Kafka broker’s JMX port is accessible to jmxtrans.
- Ensure you have a [Splunk](https://www.splunk.com/){:target="_blank"} Enterprise server installed or a Splunk Universal Forwarder that has network access to your {{site.data.resuse.icp}} cluster.
- Ensure that you have an index to receive the data and a TCP Data input configured on Splunk. Details can be found in the [Splunk documentation](https://docs.splunk.com/Documentation){:target="_blank"}.
- Ensure you have [configured access to the Docker registry](https://www.ibm.com/support/knowledgecenter/en/SSBS6K_3.1.2/manage_images/using_docker_cli.html){:target="_blank"} from the machine you will be using to deploy jmxtrans.

## jmxtrans

Jmxtrans is a connector that reads JMX metrics and outputs a number of formats supporting a wide variety of logging, monitoring, and graphing applications. To deploy to your {{site.data.resuse.icp}} cluster, you must package jmxtrans into a Kubernetes solution.

Release-specific credentials for establishing the connection between jmxtrans and the Kafka brokers are generated when {{site.data.reuse.short_name}} is installed with the **Enable secure JMX connections** selected. The credentials are stored in a Kubernetes secret inside the release namespace. See [secure JMX connections](../../2019.1.1/security/secure-jmx-connections/#providing-configuration-values) for information about the secret contents.

If you are deploying jmxtrans in a different namespace to your {{site.data.reuse.short_name}} installation, copy the secret to the required namespace with the following command:

`kubectl -n <release-namespace> get secret <release-name>-ibm-es-jmx-secret -o yaml --export | kubectl -n <target-namespace> create -f -`

The command creates the secret `<release-name>-ibm-es-jmx-secret` in the target namespace, which can then be referenced in the `jmxtrans.yaml` file later.

## Solution overview

The tasks in this tutorial help achieve the following:

1. Jmxtrans packaged into a Docker image, along with scripts to load configuration values and connection information.
2. Docker image pushed to the {{site.data.reuse.icp}} cluster Docker registry into the namespace where jmxtrans will be deployed.
3. Kubernetes pod specification created that exposes the configuration to jmxtrans via environment variables.

### Example Dockerfile

Create a `Dockerfile` as follows.

```
FROM jmxtrans/jmxtrans
COPY run.sh .
ENTRYPOINT [ "./run.sh" ]
```

### Example run.sh

Create a `run.sh` script as follows. The script generates the JSON configuration file and substitutes the release-specific connection values. It then runs jmxtrans.

```
#!/bin/sh
cat <<EOF >> /var/lib/jmxtrans/config.json
{
  "servers": [
    {
      "port": 9999,
      "host": "${JMX_HOST_0}",
      "ssl": true,
      "username": "${JMX_USER}",
      "password": "${JMX_PASSWORD}",
      "queries": [
        {
          "obj": "kafka.server:type=BrokerTopicMetrics,name=Bytes*PerSec",
          "attr": [ "Count" ],
          "outputWriters": [
            {
              "@class": "com.googlecode.jmxtrans.model.output.GraphiteWriterFactory",
              "port": 9999,
              "host": "$SPLUNK_HOST",
              "typeNames": [ "name" ],
              "flushDelayInSeconds": 5
            }
          ]
        }
      ]
    },
    {
      "port": 9999,
      "host": "${JMX_HOST_1}",
      "ssl": true,
      "username": "${JMX_USER}",
      "password": "${JMX_PASSWORD}",
      "queries": [
        {
          "obj": "kafka.server:type=BrokerTopicMetrics,name=Bytes*PerSec",
          "attr": [ "Count" ],
          "outputWriters": [
            {
              "@class": "com.googlecode.jmxtrans.model.output.GraphiteWriterFactory",
              "port": 9999,
              "host": "$SPLUNK_HOST",
              "typeNames": [ "name" ],
              "flushDelayInSeconds": 5
            }
          ]
        }
      ]
    },
    {
      "port": 9999,
      "host": "${JMX_HOST_2}",
      "ssl": true,
      "username": "${JMX_USER}",
      "password": "${JMX_PASSWORD}",
      "queries": [
        {
          "obj": "kafka.server:type=BrokerTopicMetrics,name=Bytes*PerSec",
          "attr": [ "Count" ],
          "outputWriters": [
            {
              "@class": "com.googlecode.jmxtrans.model.output.GraphiteWriterFactory",
              "port": 9999,
              "host": "$SPLUNK_HOST",
              "typeNames": [ "name" ],
              "flushDelayInSeconds": 5
            }
          ]
        }
      ]
    }
  ]
}
EOF

exec /docker-entrypoint.sh start-without-jmx
```
After you have created the file, ensure that it has execution permission by running `chmod 755 run.sh`.

### Building the Docker image

Build the Docker image as follows.

1. Ensure that the `Dockerfile` and `run.sh` are in the same directory.
2. Verify that your cluster IP is mapped to the `mycluster.icp` parameter by checking your system's host file: `cat /etc/hosts`\\
   If it is not, change the value to your cluster by editing your system’s host file: `sudo vi /etc/hosts`
3. Create a local directory, and copy the certificates file from the {{site.data.reuse.icp}} master node to the local machine:\\
  `sudo mkdir -pv /etc/docker/certs.d/mycluster.icp\:8500/`\\
  `sudo scp root@<Cluster Master Host>:/etc/docker/certs.d/mycluster.icp\:8500/ca.crt /etc/docker/certs.d/mycluster.icp\:8500/`
4. On macOS only, run the following command:\\
   `sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain /etc/docker/certs.d/mycluster.icp\:8500/ca.crt`
5. Restart Docker.
6. Log in to Docker: `docker login mycluster.icp:8500`
7. Create the image: `docker build -t mycluster.icp:8500/<target-namespace>/<image-name>:<image-version> .`
8. Push the image to your {{site.data.reuse.icp}} cluster Docker registry: `docker push mycluster.icp:8500/<target-namespace>/<image-name>:<image-version>`


### Example Kubernetes deployment file

Create a jmxtrans pod as follows.

1. Copy the following into a file called `jmxtrans.yaml`.\\
   ```
   apiVersion: v1
   kind: Pod
   metadata:
       name: jmxtrans-broker
       labels:
          app: jmxtrans-broker
   spec:
       containers:
       - name: jmxtrans
         image: <full name of docker image pushed to remote registry>
         volumeMounts:
           - name: jmx-secret-volume
             mountPath: /etc/jmx-secret
         env:
         - name: JMX_USER
           valueFrom:
             secretKeyRef:
               name: <release-name>-ibm-es-jmx-secret
               key: jmx_username
         - name: JMX_PASSWORD
           valueFrom:
             secretKeyRef:
               name: <release-name>-ibm-es-jmx-secret
               key: jmx_password
         - name: JMX_HOST_0
           value: <release-name>-ibm-es-kafka-broker-svc-0.<release-namespace>.svc
         - name: JMX_HOST_1
           value: <release-name>-ibm-es-kafka-broker-svc-1.<release-namespace>.svc
         - name: JMX_HOST_2
           value: <release-name>-ibm-es-kafka-broker-svc-2.<release-namespace>.svc
         - name: SSL_TRUSTSTORE
           value: /etc/jmx-secret/store.jks
         - name: SSL_TRUSTSTORE_PASSWORD
           valueFrom:
             secretKeyRef:
               name: <release-name>-ibm-es-jmx-secret
               key: trust_store_password
         - name: JAVA_OPTS
           value: -Djavax.net.ssl.trustStore=$(SSL_TRUSTSTORE) -Djavax.net.ssl.trustStorePassword=$(SSL_TRUSTSTORE_PASSWORD)
         # The SECONDS_BETWEEN_RUNS is the scrape frequency of the JMX values. The default value is 60 seconds. Change it to a value to suit your requirements.
         - name: SECONDS_BETWEEN_RUNS
           value: "15"
         - name: SPLUNK_HOST
           value: <splunk-hostname-or-ip-address>
       volumes:
           - name: jmx-secret-volume
             secret:
               secretName: <release-name>-ibm-es-jmx-secret
               items:
               - key: truststore.jks
                 path: store.jks
   ```
2. Create the resources in your {{site.data.reuse.icp}} cluster with the following command:\\
   `kubectl -n <target-namespace> apply -f jmxtrans.yaml`

Events start appearing in Splunk after running the command. The amount of time it takes before events appear in the Splunk index depends on a combination of the scrape interval on jmxtrans and the size of the receive queue on Splunk.

You can increase or decrease the frequency of samples in jmxtrans and the size of the receive queue. To modify the receive queue on Splunk, create an `inputs.conf` file, and specify the `queueSize` and `persistentQueueSize` settings of the `[tcp://<remote server>:<port>]` stanza.

### Troubleshooting

If metrics are not appearing in your external Splunk, check the logs for jmxtrans with the following command:

`kubectl -n <target-namespace> get logs jmxtrans-broker`

To get debug-level logs from jmxtrans, use the following steps:

1. Copy the following into a file called `logback.xml`.\\
   ```
   <configuration debug="false">
     <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
       <!-- encoders are assigned the type ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->
       <encoder>
         <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
       </encoder>
     </appender>

     <logger name="com.googlecode.jmxtrans" level="${logLevel}"/>

     <root level="info">
       <appender-ref ref="console" />
     </root>
   </configuration>
   ```
2. Add the file to the same directory as the `Dockerfile` and `run.sh` files.
3. Edit the `Dockerfile` to include the `logback.xml` file, for example:\\
   ```
   FROM jmxtrans/jmxtrans
   COPY logback.xml /usr/share/jmxtrans/conf/logback.xml
   COPY configure.sh .
   ENTRYPOINT [ "./configure.sh" ]
   ```
4. Follow the instructions for [building the docker image](#building-the-docker-image).
5. Add the following environment variable to the `jmxtrans.yaml` file after the `env:` property:\\
   ```
  {% raw %}-{% endraw %} name: JMXTRANS_OPTS
       value: -Djmxtrans.log.level=debug
   ```
6. Delete jmxtrans with the following command: `kubectl -n <target-namespace> delete pod jmxtrans-broker`
7. Check that it has been deleted with the following command `kubectl -n <target-namespace> get pods`
8. When it has been deleted, create it again with the following command: `kubectl -n <target-namespace> apply -f jmxtrans.yaml`
9. View the logs with the following command: `kubectl -n <target-namespace> get logs jmxtrans-broker`
