---
title: "Monitoring cluster health with Datadog"
description: "Monitor the health of your cluster by using Datadog to capture Kafka broker JMX metrics."
permalink: /tutorials/monitor-with-datadog/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

{{site.data.reuse.short_name}} can be configured such that Datadog can capture Kafka broker JMX metrics via its Autodiscovery service. For more information about Autodiscovery, see the [Datadog documentation](https://docs.datadoghq.com/agent/autodiscovery/){:target="_blank"}.

## Prequisites

- Ensure you have an {{site.data.reuse.short_name}} installation available. This tutorial is based on {{site.data.reuse.short_name}} version 2019.1.1.
- Ensure you have the [Datadog Kubernetes agent](https://docs.datadoghq.com/agent/?tab=agentv60){:target="_blank"} with [JMX installed](https://docs.datadoghq.com/agent/faq/docker-jmx){:target="_blank"} deployed into the {{site.data.reuse.icp}} cluster.

When installing the agent, ensure the following settings:
- The Kubernetes agent requires a less restrictive PodSecurityPolicy than required for {{site.data.reuse.short_name}}. It is recommended that you install the agent into a different [namespace](https://www.ibm.com/support/knowledgecenter/SSBS6K_3.1.2/user_management/create_project.html){:target="_blank"} than where {{site.data.reuse.short_name}} is deployed.
- For the namespace where you deploy the agent, apply a PodSecurityPolicy to allow the following:
  - volumes:
    - `hostPath`

## Configuring {{site.reuse.short_name}} for Autodiscovery

When installing {{site.data.reuse.short_name}}, firstly ensure you select the **Enable secure JMX connections** check box in the [**Kafka broker settings**](../../2019.1.1/installing/configuring/#kafka-broker-settings). This is required to ensure the Kafka brokers' JMX ports are accessible to the Datadog Agent.

Then supply the YAML object containing the required Check Templates for configuring [Kafka JMX monitoring](https://docs.datadoghq.com/integrations/kafka/){:target="_blank"}. The [example configuration supplied](https://github.com/DataDog/integrations-core/blob/master/kafka/datadog_checks/kafka/data/conf.yaml.example){:target="_blank"} provides an overview of the required fields. You can set the YAML object on the **Configuration** page by using the [configuration option](../../2019.1.1/installing/configuring/#external-monitoring) **External monitoring > Datadog - Autodiscovery annotation check templates for Kafka brokers**.

The YAML object is then applied to the Kafka pods as annotations to enable the pods to be recognized by the Datadog agent AutoDiscovery service.

The Datadog annotation format is `ad.datadoghq.com/<container identifier>.<template name>`. However, {{site.data.reuse.short_name}} automatically adds the Datadog prefix and container identifier to the annotation, so the YAML object keys must only be `<template name>` (for example `check_names`).

### Providing Check Templates

Each Check Template value is a YAML object:

```
check_names:
 - kafka
instances:
  - host: %%host%%
```

See an [example Kafka Check Template](#example-kafka-check-template-content).

### Supplying JMX connection values

As part of the Kafka `instances` Check Template, provide values to ensure the Datadog agent can communicate with the Kafka JMX port via SSL as an authenticated user.

  - `rmi_registry_ssl=true`
  - `trust_store_path=<path to trust store>`
  - `trust_store_password=<password for trust store>`
  - `user=<username for authenticating JMX connection>`
  - `password=<password for user>`

Release-specific credentials for establishing the connection are generated when {{site.data.reuse.short_name}} is installed with the **Enable secure JMX connections** selected. The credentials are stored in a Kubernetes secret inside the release namespace. See [secure JMX connections](../../2019.1.1/security/secure-jmx-connections/#providing-configuration-values) for information about the secret contents.

Because these values are not known at install time, they cannot be supplied explictly as part of the check templates configuration. [Template variables](https://docs.datadoghq.com/agent/autodiscovery/?tab=kubernetes#supported-template-variables){:target="_blank"} should be used to reference environment variables that will be supplied to each Datadog Agent pod after installing {{site.data.reuse.short_name}}. In addition, files contained inside the release-specific secret should be mounted into the Datadog Agent pod using the paths supplied in the configuration.

## Example Kafka check template content
```
check_names:
  - kafka
instances:
  - host: %%host%%
    rmi_registry_ssl: true
    #This path must be used to mound the trust store into each Datadog Agent pod
    trust_store_path: /etc/es-jmx/es-kafka-dd.jks
    #Note the environment variable used
    trust_store_password: %%env_TRUST_STORE_PASSWORD%%
    #Note the environment variable used
    user: %%env_JMX_USER%%
    #Note the environment variable used
    password: %%env_JMX_PASSWORD%%
    #Port opened on Kafka broker for JMX
    port: 9999
    tags:
      kafka: broker
init_config:
  - is_jmx: true
    conf:
    - include:
        domain: kafka.server
        bean: kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec
        attribute:
          count:
            metric_type: rate
            alias: kafka.net.bytes_in.rate
```

## Installing through Helm CLI

Check Templates can be supplied to Helm CLI installs using the following commands:

1. {{site.data.reuse.icp_cli_login}}
2. Supply `-f values.yaml` to the `helm install` command, where `values.yaml` contains:\\
   ```
   externalMonitoring:
      datadog:
        instances:
          key: value
        check_names:
          key: value
        ... remaining template values
   ```

## Configuring your Datadog agent

After installation of {{site.data.reuse.short_name}}, the DataDog DaemonSet must be edited to supply values for the environment variables and trust store referenced in the check templates.

First, the JMX secret must be copied into the Datadog Agent namespace with the following command:

`kubectl -n <release-namespace> get secret <release-name>-ibm-es-jmx-secret -o yaml --export | kubectl -n <datadog-namespace> create -f -`

That will create the secret `<release-name>-ibm-es-jmx-secret` in the DataDog namespace, which can then be referenced in the DaemonSet.

The Datadog Agent DaemonSet must now be edited to add in the following information:

```
spec:
  containers:
    - name: datadog
      env:
       #Add in new environment variables from the jmx secret. Note that the variable names match the names supplied in the `instances` check template
        - name: JMX_USER
          secretKeyRef:
            key: jmx_username
            name: <release-name>-ibm-es-jmx-secret
        - name: JMX_PASSWORD
          secretRef:
            key: jmx_password
            name: <release-name>-ibm-es-jmx-secret
        - name: TRUST_STORE_PASSWORD
          secretRef:
            key: trust_store_password
            name: <release-name>-ibm-es-jmx-secret
      ...
      # Mount the secret volume with the mount path that matches the path to the trust store in the `instances` check template
      volumeMounts:
        - name: es-volume
          mountPath: /etc/es-jmx
  ...
  volumes:
    # Mount the jmx secret as a volume, selecting the trust store item
    - name: es-volume
      fromSecret:
        secretName: <release-name>-ibm-es-jmx-secret
        items:
          - name: truststore.jks
            #Note the path should match the name of the trust store file in the `instances` check template
            path: es-kafka-dd.jks
```
