pipeline {
    agent any
    parameters {
        string(name: 'AWS_ACCOUNT_ID', defaultValue: '', description: 'AWS account id where ecs stack should be deployed')
        string(name: 'AWS_REGION', defaultValue: 'eu-west-1', description: 'AWS region where ecs stack should be deployed')
    }

    stages {

        stage('Build application jar') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('Build docker image and push to AWS registry') {
            steps {
                withCredentials([aws(credentialsId: "aws_credentials")]) {
                    sh '''
                    aws ecr get-login-password --region ${params.AWS_REGION} | docker login --username AWS --password-stdin ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com
                    aws ecr create-repository --repository-name tuicodechallenge
                    docker build -t com.example/tuicodechallenge .
                    docker tag com.example/tuicodechallenge:latest ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com/tuicodechallenge
                    docker push ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_REGION}.amazonaws.com/tuicodechallenge
                    '''
                }
            }
        }

        stage('Upload API specification to S3') {
            steps {
                withCredentials([aws(credentialsId: "aws_credentials")]) {
                    sh "aws s3 cp dist s3://apidefs"
                }
            }
        }

        stage('Deploy application stack to AWS') {
            steps {
                withCredentials([aws(credentialsId: "aws_credentials")]) {
                    //TODO replace with SAM CLI - sam package + deploy
                    sh "aws cloudformation create-stack --stack-name tui-example-ecs-stack --template-body file://aws/samTemplate.yaml  --capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND"
                }
            }
        }
    }
}

