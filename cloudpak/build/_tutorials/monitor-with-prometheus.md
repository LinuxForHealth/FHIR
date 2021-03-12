---
title: "Monitoring cluster health with Prometheus"
description: "Monitor the health of your cluster by using Prometheus to capture Kafka broker JMX metrics."
permalink: /tutorials/monitor-with-prometheus/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

You can configure {{site.data.reuse.short_name}} to allow JMX scrapers to export Kafka broker JMX metrics to external applications. This tutorial  details how to deploy a Prometheus JMX exporter into your {{site.data.reuse.icp}} cluster and export Kafka JMX metrics to an external Prometheus system.

## Prequisites

- Ensure you have an {{site.data.reuse.short_name}} installation available. This tutorial is based on {{site.data.reuse.short_name}} version 2019.1.1.
- When installing {{site.data.reuse.short_name}}, firstly ensure you select the **Enable secure JMX connections** check box in the [**Kafka broker settings**](../../2019.1.1/installing/configuring/#kafka-broker-settings). This is required to ensure the Kafka brokers' JMX ports are accessible to the Prometheus Exporter.
- Ensure you have a [Prometheus](https://prometheus.io/){:target="_blank"} server installed that has network access to your {{site.data.resuse.icp}} cluster.
- Ensure you have [configured access to the Docker registry](https://www.ibm.com/support/knowledgecenter/en/SSBS6K_3.1.2/manage_images/using_docker_cli.html){:target="_blank"} from the machine you will be using to deploy the JMX exporter.
- Ensure you have downloaded the [Prometheus JMX exporter httpserver jar file](https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_httpserver/){:target="_blank"} to the machine you will be using to deploy the JMX exporter.

## Prometheus JMX exporter

The [Prometheus JMX exporter](https://github.com/prometheus/jmx_exporter){:target="_blank"} can be run as a HTTP server which will provide an endpoint for the external Prometheus server to query for metrics data. In order to deploy to your {{site.data.resuse.icp}} cluster, the JMX exporter needs to be packaged into a Kubernetes solution.

Release-specific credentials for establishing the connection between the JMX exporter and the Kafka brokers are generated when {{site.data.reuse.short_name}} is installed with the **Enable secure JMX connections** selected. The credentials are stored in a Kubernetes secret inside the release namespace. See [secure JMX connections](../../2019.1.1/security/secure-jmx-connections/#providing-configuration-values) for information about the secret contents.

If you are deploying the JMX exporter in a different namesapce to your {{site.data.reuse.short_name}} installation, the secret must be copied to the required namespace.

`kubectl -n <release-namespace> get secret <release-name>-ibm-es-jmx-secret -o yaml --export | kubectl -n <target-namespace> create -f -`

That will create the secret `<release-name>-ibm-es-jmx-secret` in the target namespace, which can then be referenced in the `prometheus-exporter-<broker-num>.yaml` file later.

## Solution overview

The tasks in this tutorial help achieve the following:

1. JMX exporter packaged into a Docker image, along with scripts to load configuration values and connection information.
2. Docker image pushed to the {{site.data.reuse.icp}} cluster Docker registry into the namespace where the JMX exporter will be deployed.
3. Kubernetes pod specification created that exposes the configuration to the JMX exporter via environment variables and a ConfigMap.
4. Kubernetes ConfigMap containing the JMX exporter YAML configuration.
5. Kubernetes NodePort service to expose access to the JMX exporter for the external Prometheus server.

### Example Dockerfile

Create a `Dockerfile` as follows.

```
FROM <base OS Docker image with Java>
WORKDIR /opt/prometheus
COPY jmx_prometheus_httpserver.jar .
COPY run.sh .
CMD ./run.sh
```

### Example run.sh

Create a `run.sh` script as follows. The script copies the YAML configuration and appends the release-specific connection values. It then runs the JMX exporter as an HTTP server.

```
cp /etc/jmx-config/config.yaml /tmp/jmx-config.yaml

cat << EOF >> /tmp/jmx-config.yaml

ssl: true
username: ${JMX_USER}
password: ${JMX_PASSWORD}
hostPort: ${JMX_HOST}:9999
EOF

java -Djavax.net.ssl.trustStore=/etc/jmx-secret/store.jks -Djavax.net.ssl.trustStorePassword=${STORE_PW} -jar jmx_prometheus_httpserver.jar 0.0.0.0:8080 /tmp/jmx-config.yaml
```
After you have created the file, ensure that it has execution permission by running `chmod 755 run.sh`.

### Building the Docker image

Build the Docker image as follows.

1. Ensure that the `Dockerfile`, `run.sh`, and `jmx-exporter.jar` are in the same directory.
2. Verify that your cluster IP is mapped to the `mycluster.icp` parameter by checking your system's host file: `cat /etc/hosts`\\
   If it is not, change the value to your cluster by editing your system’s host file: `sudo vi /etc/hosts`
3. Create a local directory, and copy the certificates file from the {{site.data.reuse.icp}} master node to the local machine:\\
  `sudo mkdir -pv /etc/docker/certs.d/mycluster.icp\:8500/`\\
  `sudo scp root@<Cluster Master Host>:/etc/docker/certs.d/mycluster.icp\:8500/ca.crt /etc/docker/certs.d/mycluster.icp\:8500/`
4. On macOS only, run the following command:\\
   `sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain /etc/docker/certs.d/mycluster.icp\:8500/ca.crt`
5. Restart Docker.
6. Log in to Docker: `docker login mycluster.icp:8500`
7. Create the image: `docker build -t <remote-registry>:<remote-registry-port>/<target-namespace>/<image-name>:<image-version> .`
8. Push the image to your {{site.data.reuse.icp}} cluster Docker registry: `docker push <remote-registry>:<remote-registry-port>/<target-namespace>/<image-name>:<image-version>`


### Example Kubernetes deployment file

Create and expose a JMX exporter for each Kafka broker. However, the configuration ConfigMap can be shared across each instance.

1. Copy the following into a file called `prometheus-config.yaml`:\\
   ```
   ---
   apiVersion: v1
   kind: ConfigMap
   metadata:
     name: prometheus-config
   data:
     config.yaml: |-
       startDelaySeconds: 10
       lowercaseOutputName: true
       rules:
         # The following rules match the Kafka MBeans in the jconsole order
         # Broker metrics
       - pattern : kafka.server<type=BrokerTopicMetrics, name=(BytesInPerSec|BytesOutPerSec)><>(Count)
         name: kafka_server_BrokerTopicMetrics_$1_$2
   ```
2. Create the ConfigMap in your {{site.data.reuse.icp}} cluster with the following command:\\
   `kubectl -n <target-namespace> apply -f prometheus-config.yaml`
3. Copy the following into a file called `prometheus-exporter-<broker-num>.yaml`\\
   ```
   {% raw %}
   apiVersion: v1
   kind: Pod
   metadata:
       name: prometheus-export-broker-<broker-num>
       labels:
          app: prometheus-export-broker-<broker-num>
   spec:
       containers:
       - name: jmx-exporter
         image: <full name of docker image pushed to remote registry>
         volumeMounts:
           - name: jmx-secret-volume
             # mountPath must match path supplied to -Djavax.net.ssl.trustStore in run.sh
             mountPath: /etc/jmx-secret
           - name: config-volume
             # mountPath must match path supplied to the cp command in run.sh
             mountPath: /etc/jmx-config
         ports:
         - containerPort: 8080
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
         - name: JMX_HOST
           value: <release-name>-ibm-es-kafka-broker-svc-<broker-num>.<release-namespace>.svc
         - name: STORE_PW
           valueFrom:
             secretKeyRef:
               name: <release-name>-ibm-es-jmx-secret
               key: trust_store_password
       volumes:
           - name: jmx-secret-volume
             secret:
               secretName: <release-name>-ibm-es-jmx-secret
               items:
               - key: truststore.jks
                 # Path must match the filename supplied to -Djavax.net.ssl.trustStore in run.sh
                 path: store.jks
           - name: config-volume
             configMap:
                name: prometheus-config
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: prometheus-svc-<broker-num>
   spec:
     type: NodePort
     selector:
       app: prometheus-export-broker-<broker-num>
     ports:
     - port : 8080
       protocol: TCP
       {% endraw %}
   ```
4. Create the resources in your {{site.data.reuse.icp}} cluster with the following command:\\
   `kubectl -n <target-namespace> apply -f prometheus-exporter-<broker-num>.yaml`.
5. Repeat for the total number of Kafka brokers deployed in your {{site.data.reuse.short_name}} installation.
6. After the resources are created, find the `NodePort` by using the command `kubectl -n <target-namespace> get svc`. The `NodePort` is listed for the service `prometheus-svc-<broker-num>`. The connection to be supplied to your external Prometheus as static scrape target in the Prometheus configuration file is the url `<cluster name/IP>:<node-port>`

### Troubleshooting

If metrics are not appearing in your external Prometheus, check the logs for the Prometheus agent with the following command:

`kubectl -n <target-namespace> get logs prometheus-export-broker-<broker-num>`
