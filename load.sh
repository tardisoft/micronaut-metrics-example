#!/usr/bin/env bash
i=0

for ((i=1; i<=100; i++)); do
   curl -s http://localhost:8181/hello/alice > /dev/null
   curl -s http://localhost:8282/hello/bob > /dev/null
   curl -s http://localhost:8383/hello/christian > /dev/null
done
