#!/usr/bin/env node

/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

"use-strict";

var server = 'nats://127.0.0.1:4222';
var stan = require('node-nats-streaming').connect('nats-streaming', 'nats-subscriber', server);
var subscription;

stan.on('connect', function () {
  console.log('Waiting for incoming messages...Ctrl-C to exit');
  let opts = stan.subscriptionOptions().setStartWithLastReceived();
  subscription = stan.subscribe('FHIRNotificationEvent', opts);
  subscription.on('message', function (msg) {
    console.log('Received a message [' + msg.getSequence() + '] ' + msg.getSubject() + ' ' + msg.getData());
  });
});

stan.on('close', function() {
  process.exit();
});

function cleanup() {
  subscription.unsubscribe();
  subscription.on('unsubscribed', function() {
    console.log('\nUnsubscribed and closing connection');
    stan.close();
  });
}

process.on('SIGINT', cleanup);

process.on('SIGTERM', cleanup);
