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
    }

    stages {
        stage('Build Jar') {
            when {
                branch 'master'
            }
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "sudo docker build -t $DOCKER_IMAGE ."
            }
        }

        stage('Deploy') {
            steps {
                sh """
                docker stop my-spring-app || true
                docker rm my-spring-app || true
                docker run -d --name my-spring-app -p 8080:8080 \\
                  -e DB_URL=$DB_URL \\
                  -e DB_USERNAME=$DB_USERNAME \\
                  -e DB_PASSWORD=$DB_PASSWORD \\
                  -e JWT_TOKEN_SECRET=$JWT_TOKEN_SECRET \\
                  -e GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID \\
                  -e GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET \\
                  -e ELASTICSEARCH_USERNAME=$ELASTICSEARCH_USERNAME \\
                  -e ELASTICSEARCH_PASSWORD=$ELASTICSEARCH_PASSWORD \\
                  -e ACCESS_KEY=$ACCESS_KEY \\
                  -e SECRET_KEY=$SECRET_KEY \\
                  $DOCKER_IMAGE
                """
            }
        }


    }
}