node('maven') {
    stages {
        stage('Run All Tests in Parallel') {
            parallel {
                stage('API Tests') {
                    steps {
                        build job: 'Api_tests'
                    }
                }
                stage('Mobile Tests') {
                    steps {
                        build job: 'Mobile_tests'
                    }
                }
                stage('Web Tests') {
                    steps {
                        build job: 'Web_tests'
                    }
                }
            }
        }

        stage('Collect Allure Results') {
            steps {
                // Copy artifacts (allure-results) from all jobs
                copyArtifacts(projectName: 'Api_tests', filter: 'allure-results/**', flatten: true)
                copyArtifacts(projectName: 'Mobile_tests', filter: 'allure-results/**', flatten: true)
                copyArtifacts(projectName: 'Web_tests', filter: 'allure-results/**', flatten: true)
            }
        }

        stage('Generate Allure Report') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }
        }
    }
}
