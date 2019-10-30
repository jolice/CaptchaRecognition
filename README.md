# CaptchaRecognition

This is a model of a web service that allows users for solving customers' captchas for the fee, a basic implementation of the AntiGate service. 

[![Build Status](https://travis-ci.org/riguron/CaptchaRecognition.svg?branch=master)](https://travis-ci.org/riguron/CaptchaRecognition)

## Features

- Access through the secured REST API
- API key (token) authentication
- Support for both image (Base64) and text captchas
- Recognition by multiple users for more precise results
- Automatical captcha requeuing

## Running 

To start a web application, clone the project and run it:

```bash
git clone git@github.com:riguron/CaptchaRecognition.git
cd CaptchaRecognition
mvn clean compile
mvn spring-boot:run -pl Bootstrap
```

## Technologies used

- Spring Boot
- Spring MVC
- Spring Security
- Spring Data
- JUnit / Mockito
- Hibernate 5
- HSQL Database
- Lombok
- Maven

