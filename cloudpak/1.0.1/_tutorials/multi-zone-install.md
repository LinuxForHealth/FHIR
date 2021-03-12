---
title: "Installing a multizone cluster"
description: "See an example of setting up a multizone Event Streams in a non-zone-aware cluster."
permalink: /tutorials/multi-zone-tutorial/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

The following tutorial explains how to set up a [multizone](../../2019.4/installing/planning/#multizone-support) {{site.data.reuse.short_name}} cluster in a non-zone-aware cluster.

The example used in this tutorial is for installing a 3 zone cluster.

The example shows how to prepare your cluster for multiple zones by labelling your nodes, and then using those labels to set up the zones when installing {{site.data.reuse.short_name}}.

## Prerequisites

This tutorial is based on the following software versions:
- {{site.data.reuse.icp}} version 3.2.1
- {{site.data.reuse.long_name}} version 2019.4.1

## Labelling the worker nodes

To make your {{site.data.reuse.icp}} cluster zone aware, label your worker nodes to be able to specify later the zones that each node will be added to.

### Labelling for zones

Label your {{site.data.reuse.icp}} worker nodes, so that they can later be allocated to zones.

Run the following command to retrieve the nodes in your cluster:

`kubectl get nodes`

This will list the nodes of your cluster. In this example there are 12 nodes:

```
192.0.0.1 192.0.0.2 192.0.0.3 192.0.0.4 192.0.0.5 192.0.0.6 192.0.0.7 192.0.0.8 192.0.0.9 192.0.0.10 192.0.0.11 192.0.0.12

```

Using the node IP addresses, set which zone you want each node to be in. In this tutorial, you are setting up a 3 zone cluster by using the following labels, each representing a data center:
- `es-zone-0`
- `es-zone-1`
- `es-zone-2`

Label your nodes with these zone labels by using the `kubectl label nodes` command. In this example, as you are creating a 3 zone cluster, and have 12 nodes, label every 4 nodes with the same zone label.

Label the first 4 nodes `192.0.0.1-4` to allocate them to zone `es-zone-0`:

`kubectl label nodes 192.0.0.1 192.0.0.2 192.0.0.3 192.0.0.4 failure-domain.beta.kubernetes.io/zone="es-zone-0"`


Label the next 4 nodes `192.0.0.5 192.0.0.6 192.0.0.7 192.0.0.8` to allocate them to zone `es-zone-1`:

`kubectl label nodes 192.0.0.5-8 failure-domain.beta.kubernetes.io/zone="es-zone-1"`

Label the next 4 nodes `192.0.0.9 192.0.0.10 192.0.0.11 192.0.0.12` to allocate them to zone `es-zone-2`:

`kubectl label nodes 192.0.0.9-12 failure-domain.beta.kubernetes.io/zone="es-zone-2"`


As a result, all nodes that {{site.data.reuse.short_name}} will use now have a label of `failure-domain.beta.kubernetes.io/zone` with value `es-zone-0`, `es-zone-1`, or `es-zone-2`.

Run the following command to verify this:

`kubectl get nodes --show-labels`

### Labelling for Kafka

Kafka broker pods need to be distributed as evenly as possible across the zones by dedicating a node in each zone to a Kafka pod.

In this example, there are 6 Kafka brokers. This means 6 worker nodes are needed to host our Kafka brokers. Splitting the 6 brokers across 3 zones equally means you will have 2 Kafka brokers in each zone. To achieve this, label 2 nodes in each zone.

In this example, label the first 2 nodes of each zone: `192.0.0.1`, `192.0.0.2`, `192.0.0.5`, `192.0.0.6`, `192.0.0.9`, and `192.0.0.10`.

For example:

`kubectl label node 192.0.0.1 192.0.0.2 node-role.kubernetes.io/kafka=true`

### Labelling for ZooKeeper

{{site.data.reuse.short_name}} deploys 3 ZooKeeper nodes. Distribute the 3 ZooKeeper pods across the zones by dedicating 1 or 2 nodes in each zone to a ZooKeeper pod.

**Note:** Do not label more than 2 in each zone. In addition, it is preferred that you label nodes that are not already labelled with Kafka.

In this example, label the last 2 nodes of each zone: `192.0.0.3`, `192.0.0.4`, `192.0.0.7`, `192.0.0.8`, `192.0.0.11`, and `192.0.0.12`.

For example:

`kubectl label node 192.0.0.3 192.0.0.4 node-role.kubernetes.io/zk=true`


You can check that your nodes are labelled as required by using the following command:

`kubectl get nodes --show-labels`


## Installing {{site.data.reuse.short_name}}

When [installing](../../2019.4/installing/installing/) {{site.data.reuse.short_name}}, configure the Kafka broker and multizone options as follows.

If you are installing by using the UI, set the following options for the example in this tutorial:

- Set the number of Kafka brokers to 6 in the [**Kafka brokers**](../../2019.4/installing/configuring/#kafka-broker-settings) field.
- Set the  [**Number of zones**](../../2019.4/installing/configuring/#installing-into-a-multizone-cluster) field to 3.
- Enter the zone label names for each zone in the [**Zone labels**](../../2019.4/installing/configuring/#installing-into-a-multizone-cluster) field:
   ```
   es-zone-0
   es-zone-1
   es-zone-2
   ```

If you are installing by using the CLI, use the following settings for the example in this tutorial:

```
helm install --tls --name my-es --namespace=eventstreams \
--set license=accept \
--set global.image.pullSecret="my-ips" \
--set global.image.repository="<image-pull-repository>" \
--set messageIndexing.messageIndexingEnabled=false \
--set global.zones.count=3 \
--set global.zones.labels[0]="es-zone-0" \
--set global.zones.labels[1]="es-zone-1" \
--set global.zones.labels[2]="es-zone-2" \
--set kafka.brokers=6 \
ibm-eventstreams-prod
```


## Creating topics for multizone setup

It is important that you do not [configure topics](../../2019.4/administering/managing-multizone/#topic-configuration) where the minimum in-sync replicas setting cannot be met in the event of a zone failure.

**Warning:** Do not create a topic with 1 replica. Setting 1 replica means the topic will become unavailable during an outage and will lose data.

In this example, create a topic with 6 replicas, setting the minimum in-sync replicas configuration to 4. This means if a zone is lost, 2 brokers would be lost and therefore 2 replicas.

The minimum in-sync replicas would still mean the system remains operational with no data loss, as 4 brokers still remain, with four replicas of the topics data.
