pipeline {
    agent any

    parameters {
        string(
            name: 'ENV_SOURCE_DIR',
            defaultValue: '',
            description: 'Thư mục chứa local.env và .env (vd: /home/deploy hoặc để trống nếu dùng workspace/env/)'
        )
    }

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
                script {
                    def srcDir = params.ENV_SOURCE_DIR?.trim() ?: 'env'
                    echo "Nguồn env: ${srcDir} (local.env -> BE, .env -> root)"
                    sh """
                        mkdir -p Back-End
                        if [ -f '${srcDir}/local.env' ]; then
                            cp '${srcDir}/local.env' Back-End/local.env
                            echo 'Đã copy local.env -> Back-End/local.env (BE)'
                        else
                            echo 'Không thấy ${srcDir}/local.env, bỏ qua.'
                        fi
                        if [ -f '${srcDir}/.env' ]; then
                            cp '${srcDir}/.env' .env
                            echo 'Đã copy .env -> .env (root, compose + FE build)'
                        else
                            echo 'Không thấy ${srcDir}/.env, bỏ qua.'
                        fi
                    """
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('Back-End') {
                    sh 'mvn -B -q clean package -DskipTests'
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
