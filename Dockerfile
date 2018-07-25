FROM ubuntu:18.10

ARG DD_API_KEY

USER root

RUN mkdir /app
WORKDIR /app

RUN apt-get -y update && \
	apt-get -y install apt-transport-https openjdk-8-jdk curl

RUN DD_APM_ENABLED=true DD_INSTALL_ONLY=true DD_API_KEY=$DD_API_KEY bash -c "$(curl -L https://raw.githubusercontent.com/DataDog/datadog-agent/master/cmd/agent/install_script.sh)"

COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
COPY build/libs/*-all.jar metrics-all.jar
COPY dd-java-agent.jar dd-java-agent.jar
COPY datadog.example.yaml /etc/datadog-agent/datadog.example.yaml
RUN bash -c "sed 's/api_key:.*/api_key: ${DD_API_KEY}/' /etc/datadog-agent/datadog.example.yaml > /etc/datadog-agent/datadog.yaml"
RUN apt-get -y install sudo

CMD ["./entrypoint.sh"]