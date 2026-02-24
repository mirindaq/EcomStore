pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Checked out a from ${env.GIT_URL}"
            }
        }

        stage('Info') {
            steps {
                echo "Branch: ${env.GIT_BRANCH ?: 'unknown'}"
                echo "Commit: ${env.GIT_COMMIT ?: 'unknown'}"
                sh 'echo "Node: $(node -v 2>/dev/null || echo not installed)"'
                sh 'echo "Java: $(java -version 2>&1 | head -1 || echo not installed)"'
            }
        }

        stage('Build Frontend') {
            when {
                anyOf {
                    buildingTag()
                    branch 'main'
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                dir('Front-End') {
                    sh 'npm ci --prefer-offline --no-audit || npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Build Backend') {
            when {
                anyOf {
                    buildingTag()
                    branch 'main'
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                dir('Back-End') {
                    sh 'mvn -B -q clean compile -DskipTests'
                }
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
