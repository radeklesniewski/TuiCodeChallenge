pipeline {
    agent any
    parameters {
        string(name: 'AWS_ACCOUNT_ID', defaultValue: '', description: 'AWS account id where ecs stack should be deployed')
        string(name: 'AWS_REGION', defaultValue: 'eu-west-1', description: 'AWS region where ecs stack should be deployed')
    }

    stages {

        stage('Build application jar') {
            sh "mvn clean package"
        }

        stage('Build docker image and push to AWS registry') {
            sh " aws ecr get-login-password --region ${params.AWS_REGION} | docker login --username AWS --password-stdin ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com"
            sh "aws ecr create-repository --repository-name tuicodechallenge"
            sh "docker build -t com.example/tuicodechallenge ."
            sh "docker tag com.example/tuicodechallenge:latest ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com/tuicodechallenge"
            sh "docker push ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com/tuicodechallenge"
        }

        stage('Upload API specification to S3') {
            withCredentials([aws(credentialsId: "aws_credentials")]) {
                sh "aws s3 cp dist s3://apidefs"
            }
        }

        stage('Deploy application stack to AWS') {
            withCredentials([aws(credentialsId: "aws_credentials")]) {
                //TODO replace with SAM CLI - sam package + deploy
                sh "aws cloudformation create-stack --stack-name tui-example-ecs-stack --template-body file://aws/samTemplate.yaml  --capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND"
            }
        }
    }
}

