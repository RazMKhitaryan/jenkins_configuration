node('maven') {
    stage('Checkout') {
        checkout([$class: 'GitSCM',
                  branches: [[name: 'main']],
                  userRemoteConfigs: [[url: 'https://github.com/RazMKhitaryan/otus_api_helpers.git']]
        ])
    }

    stage('Run Tests') {
        sh "mvn clean test -DthreadCount=5"
    }

    stage('Allure Report Publisher') {
        // Wrap in 'try-finally' to always run
        try {
            echo "Tests finished, publishing Allure results..."
        } finally {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
            ])
        }
    }
}
