pipeline {
    agent any

    options {
        timeout(time: 45, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        COMPOSE_PROJECT_NAME = 'ecomstore'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare env files') {
            steps {
                sh '''
                    mkdir -p Back-End

                    SRC=$HOME
                    [ -d /home/deploy ] && SRC=/home/deploy

                    echo "Nguồn env: $SRC"

                    cp "$SRC/local.env" Back-End/local.env
                    cp "$SRC/.env" .env
                '''
            }
        }

        stage('Deploy (Docker Compose)') {
            steps {
                sh '''
                    docker compose pull || true
                    docker compose up -d --build
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Deploy success'
        }
        failure {
            echo '❌ Deploy failed'
        }
    }
}