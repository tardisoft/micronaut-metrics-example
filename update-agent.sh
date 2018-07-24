#!/bin/sh
#set -e
set -x

#Run this to get he latest agent jar.

wget -O dd-java-agent.jar 'https://search.maven.org/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST'