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

        stage('Prepare ENV') {
            steps {
                sh '''
                    echo "Preparing environment file..."

                    SRC=$HOME
                    [ -d /home/deploy ] && SRC=/home/deploy

                    if [ ! -f "$SRC/.env" ]; then
                        echo "❌ .env not found at $SRC"
                        exit 1
                    fi

                    cp "$SRC/.env" .env

                    echo "✅ ENV ready"
                '''
            }
        }

        stage('Deploy (Docker Compose)') {
            steps {
                sh '''
                    echo "Stopping old containers..."
                    docker compose down || true

                    echo "Building & starting services..."
                    docker compose up -d --build

                    docker compose ps
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