#!/usr/bin/env bash

sudo datadog-agent start > /var/log/datadog/agent.log 2>&1&

java -javaagent:dd-java-agent.jar -Ddd.integration.netty.enabled=true -jar metrics-all.jar