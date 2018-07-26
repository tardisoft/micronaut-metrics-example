#!/usr/bin/env bash
i=0

for ((i=1; i<=10000; i++)); do
   curl -s http://localhost:8181/hello/alice > /dev/null
   curl -s http://localhost:8181/helloworld > /dev/null
   curl -s http://localhost:8282/hello/bob > /dev/null
   curl -s http://localhost:8383/hello/christian > /dev/null
   if [[ $(( i % 100 )) == 0 ]]; then
		echo "Running $i / 10000"
   fi
done
