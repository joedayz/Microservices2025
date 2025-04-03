#!/usr/bin/env bash

mkdir microservices

cd microservices

spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=product-service \
--package-name=pe.joedayz.microservices.core.product \
--groupId=pe.joedayz.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=review-service \
--package-name=pe.joedayz.microservices.core.review \
--groupId=pe.joedayz.microservices.core.review \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
review-service

spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=recommendation-service \
--package-name=pe.joedayz.microservices.core.recommendation \
--groupId=pe.joedayz.microservices.core.recommendation \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
recommendation-service




spring init \
--boot-version=3.4.4 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=product-composite-service \
--package-name=pe.joedayz.microservices.composite.product \
--groupId=pe.joedayz.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service


cd ..