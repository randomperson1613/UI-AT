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

        script {
            int allureReportStatus = sh(script: './gradlew allureReport --no-daemon', returnStatus: true)
            if (allureReportStatus != 0) {
                echo 'Allure HTML report was not generated; Telegram notification can be skipped.'
            }
        }

        allure includeProperties: false,
               jdk: '',
               results: [[path: 'build/allure-results']]

        archiveArtifacts artifacts: 'build/reports/tests/test/**, build/selenide/**, build/allure-results/**, build/reports/allure-report/**',
                         allowEmptyArchive: true

        script {
            if (fileExists('build/reports/allure-report/allureReport/widgets/summary.json')) {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    withCredentials([
                            string(credentialsId: 'telegram-bot-token-rodneystone', variable: 'TELEGRAM_BOT_TOKEN'),
                            string(credentialsId: 'telegram-chat-id-rodneystone', variable: 'TELEGRAM_CHAT_ID')
                    ]) {
                        sh '''
                            set -eu
                            java \
                              "-DconfigFile=notifications/config.json" \
                              "-Dnotifications.base.project=${JOB_BASE_NAME}" \
                              "-Dnotifications.base.reportLink=${BUILD_URL}allure/" \
                              "-Dnotifications.telegram.token=${TELEGRAM_BOT_TOKEN}" \
                              "-Dnotifications.telegram.chat=${TELEGRAM_CHAT_ID}" \
                              -jar notifications/allure-notifications-4.11.0.jar
                        '''
                    }
                }
            } else {
                echo 'Allure summary.json not found; Telegram notification skipped.'
            }
        }
    }
}
}
