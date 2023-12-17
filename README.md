# TUI Code challenge

## Description

Microservice application for listing all non-forked GitHub repositories details for provided username
through exposed API GET endpoint "/user/{username}/repositories". Built with Spring Boot (v3.2.0), Java (v21) and Spock
framework (v2.3).

## How to run the app

[//]: # (TODO)

## REST API specification and request examples

Swagger/OpenAPI 3.0 specification can be
found [here](https://github.com/radeklesniewski/TuiCodeChallenge/blob/b954e0e255c3d3549fa5d34e1b784f285469853d/api_spec.yaml).
Few examples of curl requests & responses:

curl http://localhost:8080/user/radeklesniewski/repositories -H "Accept: application/json"

< HTTP/1.1 200

[{"repositoryName":"DoodleGenerator","ownerLogin":"radeklesniewski","
branchList":[{"branchName":"master","lastCommitSha":"92626a4b79811ffd062924217f517ae261476199"}]},{"repositoryName":"
Neuron","ownerLogin":"radeklesniewski","
branchList":[{"branchName":"master","lastCommitSha":"efbc0b82c5a2c5c592ec0d96b16de5c07631a9a1"},{"branchName":"test","lastCommitSha":"efbc0b82c5a2c5c592ec0d96b16de5c07631a9a1"}]},{"
repositoryName":"ScatterSearchForTSP","ownerLogin":"radeklesniewski","
branchList":[{"branchName":"master","lastCommitSha":"1f4dd5d9c11365f87faf7c6b03b4ccfd5686e690"}]}]

curl http://localhost:8080/user/radeklesniewski/repositories -H "Accept: application/xml"

< HTTP/1.1 406

{"message":"No acceptable representation","status":406}

curl http://localhost:8080/user/nonExistingUser/repositories -H "Accept: application/json" -v

< HTTP/1.1 404

{"message":"Provided username could not be found.","status":404}

## Unit & Integration tests

[Unit tests (Spock)](https://github.com/radeklesniewski/TuiCodeChallenge/blob/33dbbf21745bce83e32fff5f46e10738f618329f/src/test/groovy/com/example/tuicodechallenge)

[Integration tests (JUnit)](https://github.com/radeklesniewski/TuiCodeChallenge/blob/33dbbf21745bce83e32fff5f46e10738f618329f/src/test/java/com/example/tuicodechallenge)

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