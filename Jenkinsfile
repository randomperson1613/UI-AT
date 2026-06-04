pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '10'))
    }

    environment {
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                sh '''
                    set -eu
                    java -version
                    javac -version
                    if command -v google-chrome >/dev/null 2>&1; then
                        google-chrome --version
                    else
                        google-chrome-stable --version
                    fi
                    chmod +x ./gradlew
                '''
            }
        }

        stage('UI Tests') {
            steps {
                sh './gradlew clean test -Dheadless=true -Dbrowser=chrome -DchromeNoSandbox=true --no-daemon'
            }
        }
    }

    post {
        always {
            junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true

            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'build/allure-results']]

            archiveArtifacts artifacts: 'build/reports/tests/test/**, build/selenide/**, build/allure-results/**',
                             allowEmptyArchive: true
        }
    }
}
