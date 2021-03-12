---
title: "Setting up alert notifications to Slack"
description: "Receive notifications about the health of your cluster based on monitored metrics."
permalink: /tutorials/monitoring-alerts/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

Receiving notifications based on monitored metrics is an important way of keeping an eye on the health of your cluster. You can set up notifications to be sent to applications like Slack based on pre-defined triggers.

The following tutorial shows an example of how to set up alert notifications to be sent to Slack based on metrics from {{site.data.reuse.short_name}}.

## Prerequisites

- Ensure you have an {{site.data.reuse.short_name}} installation available. This tutorial is based on {{site.data.reuse.short_name}} version 2019.1.1 installed on {{site.data.reuse.icp}} 3.1.1, using the default master port 8443.
- Ensure you have [Slack](https://slack.com/){:target="_blank"} installed and ready to use. This tutorial is based on Slack version 3.3.8.
- You need to be a Workplace Administrator to add apps to a Slack channel.

## Preparing Slack

To send notifications from {{site.data.reuse.short_name}} to your Slack channel, configure an incoming webhook URL within your Slack service. The webhook URL provided by Slack is required for the integration steps later in this section. To create the webhook URL:

1. Open Slack and go to your Slack channel where you want the notifications to be sent.
2. From your Slack channel click the icon for **Channel Settings**, and select **Add apps** or **Add an app** depending on the Slack plan you are using.
3. Search for "Incoming Webhooks".
4. Click **Add configuration**.
5. Select the channel that you want to post to.
6. Click **Add Incoming Webhooks integration**.
7. Copy the URL in the **Webhook URL** field.

For more information about incoming webhooks in Slack, see the [Slack documentation](https://api.slack.com/incoming-webhooks){:target="_blank"}.

## Selecting the metric to monitor

To retrieve a list of available metrics, use an HTTP GET request on your ICP cluster URL as follows:

1. Log in to your IBM Cloud Private cluster management console from a supported web browser by using the URL `https://<Cluster Master Host>:8443`.
2. Use the following request: `https://<Cluster Master Host>:8443/prometheus/api/v1/label/__name__/values`\\
   The list of available metrics is displayed.
3. Select a metric to monitor.

For example, to test the triggering of alerts, you can monitor the total number of partitions for all topics by using the `kafka_server_replicamanager_partitioncount_value` metric. When topics are created, this metric can trigger notifications.

For production environments, a good metric to monitor is the number of under-replicated partitions as it tells you about potential problems with your Kafka cluster, such as load or network problems where the cluster becomes overloaded and followers are not able to catch up on leaders. Under-replicated partitions might be a temporary problem, but if it continues for longer, it probably requires urgent attention. An example is to set up a notification trigger to your Slack channel if the number of under-replicated partitions is greater than 0 for more than a minute. You can do this with the `kafka_server_replicamanager_underreplicatedpartitions_value` metric.

The examples in this tutorial show you how to set up monitoring for both of these metrics, with the purpose of testing notification triggers, and also to have a production environment example.

**Note:** Not all of the metrics that Kafka uses are published to Prometheus by default. The metrics that are published are controlled by a ConfigMap. You can publish metrics by adding them to the ConfigMap.

For information about the different metrics, see [Monitoring Kafka](https://kafka.apache.org/documentation/#monitoring){:target="_blank"}.

## Setting the alert rule

To set up the alert rule and define the trigger criteria, use the `monitoring-prometheus-alertrules` ConfigMap.

By default, the list of rules is empty. See the `data` section of the ConfigMap, for example:

```
user$ kubectl get configmap -n kube-system monitoring-prometheus-alertrules -o yaml

apiVersion: v1
data:
  alert.rules: ""
kind: ConfigMap
metadata:
  creationTimestamp: 2019-04-05T13:07:48Z
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.2.0
    component: prometheus
    heritage: Tiller
    release: monitoring
  name: monitoring-prometheus-alertrules
  namespace: kube-system
  resourceVersion: "4564"
  selfLink: /api/v1/namespaces/kube-system/configmaps/monitoring-prometheus-alertrules
  uid: a87b5766-c89f-11e8-9f94-00000a3304c0
```

### Example test setup

As mentioned earlier, to test the triggering of alerts, you can monitor the total number of partitions for all topics by using the `kafka_server_replicamanager_partitioncount_value` metric.

Define an alert rule that creates a notification if the number of partitions increases. To achieve this, add a new rule for `kafka_server_replicamanager_partitioncount_value`, and set the trigger conditions in the `data` section, for example:

**Note:** In this example, we are setting a threshold value of 50 as the built-in consumer-offsets topic has 50 partitions by default already, and this topic is automatically created the first time a consumer application connects to the cluster. We will create a topic later with 10 partitions to [test](#testing) the firing of the alert and the subsequent notification to the Slack channel.

```
{% raw %}user$ kubectl edit configmap -n kube-system monitoring-prometheus-alertrules

apiVersion: v1
data:
  sample.rules: |-
    groups:
    - name: alert.rules
      #
      # Each of the alerts you want to create will be listed here
      rules:
      # Posts an alert if the number of partitions increases
      - alert: PartitionCount
        expr: kafka_server_replicamanager_partitioncount_value > 50
        for: 10s
        labels:
          # Labels should match the alert manager so that it is received by the Slack hook
          severity: critical
        # The contents of the Slack messages that are posted are defined here
        annotations:
          identifier: "Partition count"
          description: "There are {{ $value }} partition(s) reported by broker {{ $labels.kafka }}"
kind: ConfigMap
metadata:
  creationTimestamp: 2019-04-05T13:07:48Z
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.2.0
    component: prometheus
    heritage: Tiller
    release: monitoring
  name: monitoring-prometheus-alertrules
  namespace: kube-system
  resourceVersion: "84156"
  selfLink: /api/v1/namespaces/kube-system/configmaps/monitoring-prometheus-alertrules
  uid: a87b5766-c89f-11e8-9f94-00000a3304c0{% endraw %}
```

**Important:** As noted in the prerequisites, this tutorial is based on {{site.data.reuse.icp}} 3.1.1. Setting up alert rules is different if you are using {{site.data.reuse.icp}} 3.1.2 or later, as each alert rule is a dedicated Kubernetes resource instead of being defined in a ConfigMap.

This means that instead of adding alert rule entries to a ConfigMap, you create a separate alert rule resource for each alert you want to enable.

In addition, the alert rules don't need to be in the `kube-system` namespace, they can be added to the namespace where your release is deployed. This also means you don't have to be a Cluster administrator to add alert rules.

For example, to create the rule by using a dedicated alert rule, you can save it to a file as follows:

```
{% raw %}apiVersion: monitoringcontroller.cloud.ibm.com/v1
kind: AlertRule
metadata:
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.4.0
    component: prometheus
    heritage: Tiller
    release: RELEASENAME
  name: partition-count
  namespace: NAMESPACE
spec:
  data: |-
    groups:
      - name: PartitionCount
        rules:
          - alert: PartitionCount
            expr: kafka_server_replicamanager_partitioncount_value > 50
            for: 10s
            labels:
              severity: critical
            annotations:
              identifier: 'Partition count'
              description: 'There are {{ $value }} partition(s) reported by broker {{ $labels.kafka }}'
  enabled: true{% endraw %}
```

To review your alert rules set up this way, use the `kubectl get alertrules` command, for example:

```
$ kubectl get alertrules
NAME                          ENABLED   AGE   CHART                     RELEASE         ERRORS
partition-count               true      1h    ibm-icpmonitoring-1.4.0   es-demo
```

### Example production setup

As mentioned earlier, a good metric to monitor in production environments is the metric `kafka_server_replicamanager_underreplicatedpartitions_value`, for which we want to define an alert rule that creates a notification if the number of under-replicated partitions is greater than 0 for more than a minute. To achieve this, add a new rule for `kafka_server_replicamanager_underreplicatedpartitions_value`, and set the trigger conditions in the `data` section, for example:

```
user$ kubectl edit configmap -n kube-system monitoring-prometheus-alertrules

apiVersion: v1
data:
  sample.rules: |-
    groups:
    - name: alert.rules
      #
      # Each of the alerts you want to create will be listed here
      rules:
      # Posts an alert if there are any under-replicated partitions
      #  for longer than a minute
      - alert: under_replicated_partitions
        expr: kafka_server_replicamanager_underreplicatedpartitions_value > 0
        for: 1m
        labels:
          # Labels should match the alert manager so that it is received by the Slack hook
          severity: critical
        # The contents of the Slack messages that are posted are defined here
        annotations:
          identifier: "Under-replicated partitions"
          description: "There are {% raw %}{{ $value }}{% endraw %} under-replicated partition(s) reported by broker {% raw %}{{ $labels.kafka }}{% endraw %}"
kind: ConfigMap
metadata:
  creationTimestamp: 2019-04-05T13:07:48Z
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.2.0
    component: prometheus
    heritage: Tiller
    release: monitoring
  name: monitoring-prometheus-alertrules
  namespace: kube-system
  resourceVersion: "84156"
  selfLink: /api/v1/namespaces/kube-system/configmaps/monitoring-prometheus-alertrules
  uid: a87b5766-c89f-11e8-9f94-00000a3304c0
```

**Important:** As noted in the prerequisites, this tutorial is based on {{site.data.reuse.icp}} 3.1.1. Setting up alert rules is different if you are using {{site.data.reuse.icp}} 3.1.2 or later, as each alert rule is a dedicated Kubernetes resource instead of being defined in a ConfigMap.

This means that instead of adding alert rule entries to a ConfigMap, you create a separate alert rule resource for each alert you want to enable.

In addition, the alert rules don't need to be in the `kube-system` namespace, they can be added to the namespace where your release is deployed. This also means you don't have to be a Cluster administrator to add alert rules.

For example, to create the rule by using a dedicated alert rule, you can save it to a file as follows:

```
{% raw %}apiVersion: monitoringcontroller.cloud.ibm.com/v1
kind: AlertRule
metadata:
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.4.0
    component: prometheus
    heritage: Tiller
    release: RELEASENAME
  name: under-replicated-partitions
  namespace: NAMESPACE
spec:
  data: |-
    groups:
      - name: UnderReplicatedPartitions
        rules:
          - alert: UnderReplicatedPartitions
            expr: kafka_server_replicamanager_underreplicatedpartitions_value > 0
            for: 1m
            labels:
              severity: critical
            annotations:
              identifier: 'Under-replicated partitions'
              description: 'There are {{ $value }} under-replicated partition(s) reported by broker {{ $labels.kafka }}'
  enabled: true{% endraw %}
```

To review your alert rules set up this way, use the `kubectl get alertrules` command, for example:

```
$ kubectl get alertrules
NAME                          ENABLED   AGE   CHART                     RELEASE         ERRORS
under-replicated-partitions   true      1h    ibm-icpmonitoring-1.4.0   es-prod
```

## Defining the alert destination

To define where to send the notifications triggered by the alert rule, specify Slack as a receiver by adding details about your Slack channel and the webhook you copied earlier to the `monitoring-prometheus-alertmanager` ConfigMap. For more information about Prometheus Alertmanager, see the [Prometheus documentation](https://prometheus.io/docs/alerting/configuration/){:target="_blank"}.

By default, the list of receivers is empty. See the `data` section of the ConfigMap, for example:

```
user$ kubectl get configmap -n kube-system monitoring-prometheus-alertmanager -o yaml

apiVersion: v1
data:
  alertmanager.yml: |-
    global:
    receivers:
      - name: default-receiver
    route:
      group_wait: 10s
      group_interval: 5m
      receiver: default-receiver
      repeat_interval: 3h
kind: ConfigMap
metadata:
  creationTimestamp: 2019-04-05T13:07:48Z
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.2.0
    component: alertmanager
    heritage: Tiller
    release: monitoring
  name: monitoring-prometheus-alertmanager
  namespace: kube-system
  resourceVersion: "4565"
  selfLink: /api/v1/namespaces/kube-system/configmaps/monitoring-prometheus-alertmanager
  uid: a87bdb44-c89f-11e8-9f94-00000a3304c0
```

Define the Slack channel as the receiver using the incoming webhook you copied earlier, and also set up the notification details such as the channel to post to, the content format, and criteria for the events to send to Slack. Settings to configure include the following:

- `slack_api_url`: The incoming webhook generated in Slack earlier.
- `send_resolved`: Set to `true` to send notifications about resolved alerts.
- `channel`: The Slack channel to send the notifications to.
- `username`: The username that posts the alert notifications to the channel.

For more information about the configuration settings to enter for Slack notifications, see the [Prometheus documentation](https://prometheus.io/docs/alerting/configuration/#slack_config){:target="_blank"}.

The content for the posts can be customized, see the following [blog](https://medium.com/quiq-blog/better-slack-alerts-from-prometheus-49125c8c672b){:target="_blank"} for Slack alert examples from Prometheus.

For example, to set up Slack notifications for your alert rule created earlier:

```
user$ kubectl edit configmap -n kube-system monitoring-prometheus-alertmanager
apiVersion: v1
data:
  alertmanager.yml: |-
    global:
      # This is the URL for the Incoming Webhook you created in Slack
      slack_api_url:  https://hooks.slack.com/services/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
    receivers:
      - name: default-receiver
        #
        # Adding a Slack channel integration to the default Prometheus receiver
        #  see https://prometheus.io/docs/alerting/configuration/#slack_config
        #  for details about the values to enter
        slack_configs:
        - send_resolved: true

          # The name of the Slack channel that alerts should be posted to
          channel: "#ibm-eventstreams-demo"

          # The username to post alerts as
          username: "IBM Event Streams"

          # An icon for posts in Slack
          icon_url: https://developer.ibm.com/messaging/wp-content/uploads/sites/18/2018/09/icon_dev_32_24x24.png

          #
          # The content for posts to Slack when alert conditions are fired
          # Improves on the formatting from the default, with support for handling
          #  alerts containing multiple events.
          # (Modified from the examples in
          #   https://medium.com/quiq-blog/better-slack-alerts-from-prometheus-49125c8c672b)
          title: |-{% raw %}
            [{{ .Status | toUpper }}{{ if eq .Status "firing" }}:{{ .Alerts.Firing | len }}{{ end }}] {{ if or (and (eq (len .Alerts.Firing) 1) (eq (len .Alerts.Resolved) 0)) (and (eq (len .Alerts.Firing) 0) (eq (len .Alerts.Resolved) 1)) }}{{ range .Alerts.Firing }} @ {{ .Annotations.identifier }}{{ end }}{{ range .Alerts.Resolved }} @ {{ .Annotations.identifier }}{{ end }}{{ end }}
          text: |-
            {{ if or (and (eq (len .Alerts.Firing) 1) (eq (len .Alerts.Resolved) 0)) (and (eq (len .Alerts.Firing) 0) (eq (len .Alerts.Resolved) 1)) }}
            {{ range .Alerts.Firing }}{{ .Annotations.description }}{{ end }}{{ range .Alerts.Resolved }}{{ .Annotations.description }}{{ end }}
            {{ else }}
            {{ if gt (len .Alerts.Firing) 0 }}
            *Alerts Firing:*
            {{ range .Alerts.Firing }}- {{ .Annotations.identifier }}: {{ .Annotations.description }}
            {{ end }}{{ end }}
            {{ if gt (len .Alerts.Resolved) 0 }}
            *Alerts Resolved:*
            {{ range .Alerts.Resolved }}- {{ .Annotations.identifier }}: {{ .Annotations.description }}
            {{ end }}{{ end }}
            {{ end }}{% endraw %}
    route:
      group_wait: 10s
      group_interval: 5m
      receiver: default-receiver
      repeat_interval: 3h
      #
      # The criteria for events that should go to Slack
      routes:
      - match:
          severity: critical
        receiver: default-receiver
kind: ConfigMap
metadata:
  creationTimestamp: 2019-04-05T13:07:48Z
  labels:
    app: monitoring-prometheus
    chart: ibm-icpmonitoring-1.2.0
    component: alertmanager
    heritage: Tiller
    release: monitoring
  name: monitoring-prometheus-alertmanager
  namespace: kube-system
  resourceVersion: "4565"
  selfLink: /api/v1/namespaces/kube-system/configmaps/monitoring-prometheus-alertmanager
  uid: a87bdb44-c89f-11e8-9f94-00000a3304c0
```

To check that the new alert is set up, use the Prometheus UI as follows:

1. Log in to your IBM Cloud Private cluster management console from a supported web browser by using the URL `https://<Cluster Master Host>:8443`.
2. Go to the Prometheus UI at `https://<Cluster Master Host>:8443/prometheus`, and click the **Alerts** tab to see the active alerts. You can also go to **Status > Rules** to view the defined alert rules.

For example:

![Prometheus alert rules](../../images/alert_rules.png "Screen capture showing the alert rule for the under_replicated_partitions alert in the Prometheus UI.")

![Prometheus alert rules](../../images/alerts.png "Screen capture showing the details for the under_replicated_partitions alert in the Prometheus UI.")

## Testing

### Example test setup

To create a notification for the test setup, create a topic with 10 partitions as follows:

1. Log in to your IBM Event Streams UI.
2. Click the **Topics** tab.
3. Click **Create topic**.
4. Follow the instructions to create the topic, and set the **Partitions** value to 10.

The following notification is sent to the Slack channel when the topic is created:

![Alert firing message posted to Slack channel in test example](../../images/slack_alert_firing_test.png "Screen capture showing the firing message posted to the Slack channel by the PartitionCount alert.")

To create a resolution alert, delete the topic you created previously:

1. Log in to your IBM Event Streams UI.
2. Click the **Topics** tab.
3. Go to the topic you created and click ![More options icon](../../images/more_options.png "Three vertical dots for the more options icon at end of each row."){:height="30px" width="15px"} **More options > Delete this topic**.

When the topic is deleted, the following resolution alert is posted:

![Alert resolved message posted to Slack channel in test example](../../images/slack_alert_resolved_test.png "Screen capture showing the resolved message posted to the Slack channel by the PartitionCount alert.")

### Example production setup

For the production environment example, the following notification is posted to the Slack channel if the number of under-replicated partitions remains above 0 for a minute:

![Alert firing message posted to Slack channel in production example](../../images/slack_alert_firing.png "Screen capture showing the firing message posted to the Slack channel by the under_replicated_partitions alert.")

When the cluster recovers, a new resolution alert is posted when the number of under-replicated partitions returns to 0. This is based on the `send_resolved` setting (was set to `true`).

![Alert resolved message posted to Slack channel in production example](../../images/slack_alert_resolved.png "Screen capture showing the resolved message posted to the Slack channel by the under_replicated_partitions alert.")

## Setting up other notifications

You can use this example to set up alert notifications to other applications, including [HipChat](https://prometheus.io/docs/alerting/configuration/#hipchat_config){:target="_blank"}, [PagerDuty](https://prometheus.io/docs/alerting/configuration/#pagerduty_config){:target="_blank"}, [emails](https://prometheus.io/docs/alerting/configuration/#email_config){:target="_blank"}, and so on. You can also use this technique to generate HTTP calls, which lets you customize alerts when defining a flow in tools like [Node-RED](https://nodered.org/){:target="_blank"} or [IBM App Connect](https://developer.ibm.com/integration/docs/){:target="_blank"}.
