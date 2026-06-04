pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '10'))
    }

    parameters {
        booleanParam(name: 'REMOTE_DRIVER', defaultValue: true, description: 'Run tests in Selenoid instead of local Chrome')
        string(name: 'SELENOID_URL', defaultValue: 'https://selenoid.autotests.cloud/wd/hub', description: 'Remote WebDriver URL without credentials')
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Browser name')
        string(name: 'BROWSER_VERSION', defaultValue: '', description: 'Browser version in Selenoid; leave empty to use default')
        string(name: 'BROWSER_SIZE', defaultValue: '1920x1080', description: 'Browser window size')
        booleanParam(name: 'HEADLESS', defaultValue: false, description: 'Use false for Selenoid video; true is useful for local CI Chrome')
        booleanParam(name: 'ENABLE_VIDEO', defaultValue: true, description: 'Enable Selenoid video recording')
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
                    chmod +x ./gradlew
                '''

                script {
                    boolean remoteDriver = params.REMOTE_DRIVER == null ? true : params.REMOTE_DRIVER
                    if (remoteDriver) {
                        echo 'Remote Selenoid run is enabled; local Chrome check skipped.'
                    } else {
                        sh '''
                            set -eu
                            if command -v google-chrome >/dev/null 2>&1; then
                                google-chrome --version
                            else
                                google-chrome-stable --version
                            fi
                        '''
                    }
                }
            }
        }

        stage('UI Tests') {
            steps {
                script {
                    boolean remoteDriver = params.REMOTE_DRIVER == null ? true : params.REMOTE_DRIVER
                    String browser = params.BROWSER?.trim() ?: 'chrome'
                    String browserVersion = params.BROWSER_VERSION?.trim() ?: ''
                    String browserSize = params.BROWSER_SIZE?.trim() ?: '1920x1080'
                    boolean headless = params.HEADLESS == null ? false : params.HEADLESS
                    boolean enableVideo = params.ENABLE_VIDEO == null ? true : params.ENABLE_VIDEO

                    if (remoteDriver) {
                        String selenoidUrl = params.SELENOID_URL?.trim() ?: 'https://selenoid.autotests.cloud/wd/hub'

                        withCredentials([
                                usernamePassword(credentialsId: 'selenoid-autotests-cloud',
                                        usernameVariable: 'REMOTE_USER',
                                        passwordVariable: 'REMOTE_PASSWORD')
                        ]) {
                            withEnv([
                                    "SELENOID_URL=${selenoidUrl}",
                                    "BROWSER=${browser}",
                                    "BROWSER_VERSION=${browserVersion}",
                                    "BROWSER_SIZE=${browserSize}",
                                    "HEADLESS=${headless}",
                                    "ENABLE_VIDEO=${enableVideo}"
                            ]) {
                                sh '''
                                    set +x
                                    set -eu
                                    ./gradlew clean test \
                                      -Dremote="${SELENOID_URL}" \
                                      -Dbrowser="${BROWSER}" \
                                      -DbrowserVersion="${BROWSER_VERSION}" \
                                      -DbrowserSize="${BROWSER_SIZE}" \
                                      -Dheadless="${HEADLESS}" \
                                      -DenableVideo="${ENABLE_VIDEO}" \
                                      -DsessionName="${JOB_NAME} #${BUILD_NUMBER}" \
                                      --no-daemon
                                '''
                            }
                        }
                    } else {
                        withEnv([
                                "BROWSER=${browser}",
                                "BROWSER_VERSION=${browserVersion}",
                                "BROWSER_SIZE=${browserSize}",
                                "HEADLESS=${headless}"
                        ]) {
                            sh '''
                                set -eu
                                ./gradlew clean test \
                                  -Dbrowser="${BROWSER}" \
                                  -DbrowserVersion="${BROWSER_VERSION}" \
                                  -DbrowserSize="${BROWSER_SIZE}" \
                                  -Dheadless="${HEADLESS}" \
                                  -DchromeNoSandbox=true \
                                  --no-daemon
                            '''
                        }
                    }
                }
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
                                cat > notifications/config-runtime.json <<EOF
{
  "base": {
    "project": "${JOB_BASE_NAME}",
    "environment": "practice.expandtesting.com",
    "comment": "UI autotests",
    "reportLink": "${BUILD_URL}allure/",
    "language": "ru",
    "allureFolder": "build/reports/allure-report/allureReport",
    "enableChart": true,
    "enableSuitesPublishing": false,
    "customData": {}
  },
  "telegram": {
    "token": "${TELEGRAM_BOT_TOKEN}",
    "chat": "${TELEGRAM_CHAT_ID}",
    "replyTo": "",
    "templatePath": "/templates/telegram.ftl"
  }
}
EOF

                                java "-DconfigFile=notifications/config-runtime.json" \
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
