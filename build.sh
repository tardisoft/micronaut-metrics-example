#!/usr/bin/env bash

./gradlew clean build

docker build . --tag micronaut-metrics:latest
