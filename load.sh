#!/usr/bin/env bash
i=0
max=10000

for ((i=1; i<=max; i++)); do
   curl -s http://localhost:8181/hello/alice${i} > /dev/null
   curl -s http://localhost:8181/helloworld > /dev/null
   curl -s http://localhost:8282/hello/bob${i} > /dev/null
   curl -s http://localhost:8383/hello/christian${i} > /dev/null
   curl -s http://localhost:8181/books/all > /dev/null
   if [[ $(( i % 100 )) == 0 ]]; then
		echo "Running $i / $max"
   fi
done
