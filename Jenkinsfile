pipeline {
    agent any

    options {
        skipDefaultCheckout() // 중복 체크아웃 방지
    }

    environment {
        DB_URL = credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        JWT_TOKEN_SECRET = credentials('jwt-secret')
        GOOGLE_CLIENT_ID = credentials('google-client-id')
        GOOGLE_CLIENT_SECRET = credentials('google-client-secret')
        ELASTICSEARCH_USERNAME = credentials('elastic-username')
        ELASTICSEARCH_PASSWORD = credentials('elastic-password')
        ACCESS_KEY = credentials('aws-access-key')
        SECRET_KEY = credentials('aws-secret-key')
        DOCKER_IMAGE = 'noc-backend'
        ELASTICSEARCH_URL = credentials('elasticsearch-url')
        REDIS_HOST = credentials('redis-host')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Jar') {
//             when {
//                 expression {
//                     def branchName = sh(script: "git name-rev --name-only HEAD", returnStdout: true).trim()
//                     return branchName == 'origin/master' || branchName == 'origin/issue/53'
//                 }
//             }
            steps {
                sh '''
                chmod +x ./gradlew
                ./gradlew clean build -x test
                '''
            }
        }

        stage('Clean Old Docker Image') {
            steps {
                sh '''
                echo "Removing old Docker image if exists..."
                docker rmi -f noc-backend || true
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker buildx build \
                  --platform linux/arm64 \
                  --cache-from=type=local,src=/tmp/.buildx-cache \
                  --cache-to=type=local,dest=/tmp/.buildx-cache,mode=max \
                  -t noc-backend --load .
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh script: '''#!/bin/bash
                docker stop my-spring-app || true
                docker rm my-spring-app || true
                docker run -d --name my-spring-app -p 8080:8080 \\
                  --add-host=host.docker.internal:host-gateway \
                  -e DB_URL="$DB_URL" \\
                  -e DB_USERNAME="$DB_USERNAME" \\
                  -e DB_PASSWORD="$DB_PASSWORD" \\
                  -e JWT_TOKEN_SECRET="$JWT_TOKEN_SECRET" \\
                  -e GOOGLE_CLIENT_ID="$GOOGLE_CLIENT_ID" \\
                  -e GOOGLE_CLIENT_SECRET="$GOOGLE_CLIENT_SECRET" \\
                  -e ELASTICSEARCH_USERNAME="$ELASTICSEARCH_USERNAME" \\
                  -e ELASTICSEARCH_PASSWORD="$ELASTICSEARCH_PASSWORD" \\
                  -e ACCESS_KEY="$ACCESS_KEY" \\
                  -e SECRET_KEY="$SECRET_KEY" \\
                    -e ELASTICSEARCH_URL="$ELASTICSEARCH_URL" \\
                    -e REDIS_HOST="$REDIS_HOST" \\
                  $DOCKER_IMAGE
                '''
            }
        }


    }
}