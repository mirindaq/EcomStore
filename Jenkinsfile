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
                echo "Repository: ${env.GIT_URL}, Branch: ${env.GIT_BRANCH ?: 'unknown'}"
            }
        }

        stage('Prepare env files') {
            steps {
                sh '''
                    mkdir -p Back-End
                    SRC=$HOME
                    [ -d /home/deploy ] && SRC=/home/deploy
                    echo "Nguồn env: $SRC"
                    if [ -f "$SRC/local.env" ]; then
                        cp "$SRC/local.env" Back-End/local.env
                        echo "Đã copy local.env -> Back-End/local.env (BE)"
                    else
                        echo "Không thấy $SRC/local.env"; exit 1
                    fi
                    if [ -f "$SRC/.env" ]; then
                        cp "$SRC/.env" .env
                        echo "Đã copy .env -> .env (root)"
                    else
                        echo "Không thấy $SRC/.env"; exit 1
                    fi
                '''
            }
        }

        stage('Build Backend') {
            steps {
                dir('Back-End') {
                    sh './mvnw -B -q clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Front-End') {
                    sh 'npm ci --prefer-offline --no-audit || npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Docker: Build images') {
            steps {
                sh 'docker compose build backend frontend'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
