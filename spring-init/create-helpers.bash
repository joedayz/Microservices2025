#!/usr/bin/env bash

spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=api \
--package-name=pe.joedayz.microservices.api \
--groupId=pe.joedayz.microservices.api \
--dependencies=webflux \
--version=1.0.0-SNAPSHOT \
api

spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=util \
--package-name=pe.joedayz.microservices.util \
--groupId=pe.joedayz.microservices.util \
--dependencies=webflux \
--version=1.0.0-SNAPSHOT \
util

