# TUI Code challenge

## Description

Spring Boot (v3.2.0) Java (v21) microservice application for listing all GitHub repositories for provided username
through exposed GET endpoint "/user/{username}/repositories"

## How to run the app

[//]: # (TODO)

## HTTP API request examples

## Requirements

### Acceptance criteria

1. As an api consumer, given username and header “Accept: application/json”, I would
   like to list all his github repositories, which are not forks. Information, which I require
   in the response, is:

   a. Repository Name

   b. Owner Login

   c. For each branch it’s name and last commit sha

2. As an api consumer, given not existing github user, I would like to receive 404
   responses in such a format:
   {“status”: ${responseCode}, “message”: ${whyHasItHappened} }

3. As an api consumer, given header “Accept: application/xml”, I would like to
   receive 406 responses in such a format:
   {“status”: ${responseCode}, “message”: ${whyHasItHappened} }

Notes

1. Please full-fill the given acceptance criteria, delivering us your best code compliant
   with industry standards.
2. Please use https://developer.github.com/v3 as a backing API
3. Please write your api spec in swagger yaml file
4. Application should have a proper README.md file
5. Please use any reactive framework of your choice (Java or NodeJs preferably)
6. Proper unit and integration test cases
   Extended challenge

It is not mandatory to do the below deployment setup that is part of the extended challenge
but if provided it will be added points to the assessment and will also confirm your devops
working knowledge.
Deployment setup:

1. Prepare Dockerfile so that app can be run in a container
2. Prepare CloudFormation scripts so that the containerized app will be run as a Fargate service (or directly in ECS you
   choose)
3. Prepare CloudFormation scripts, that will create API Gateway and
   expose the app thru it
4. Prepare Jenkins pipeline, that will build the app and deploy it to AWS using scripts
   from steps above (use free tier AWS account)

