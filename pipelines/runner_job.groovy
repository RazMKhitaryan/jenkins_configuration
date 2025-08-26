node('maven') {

    stage('Run All Tests in Parallel') {
        parallel(
                'API Tests': {
                    script {
                        build job: 'Api_tests', propagate: false
                    }
                },
                'Mobile Tests': {
                    script {
                        build job: 'Mobile_tests', propagate: false
                    }
                },
                'Web Tests': {
                    script {
                        build job: 'Web_tests', propagate: false
                    }
                }
        )
    }

    stage('Collect Allure Results') {
        script {
            copyArtifacts(
                    projectName: 'Api_tests',
                    selector: lastSuccessful(),
                    filter: 'allure-results/**',
                    target: 'allure-results/api',
                    flatten: true
            )
            copyArtifacts(
                    projectName: 'Mobile_tests',
                    selector: lastSuccessful(),
                    filter: 'allure-results/**',
                    target: 'allure-results/mobile',
                    flatten: true
            )
            copyArtifacts(
                    projectName: 'Web_tests',
                    selector: lastSuccessful(),
                    filter: 'allure-results/**',
                    target: 'allure-results/web',
                    flatten: true
            )
        }
    }

    stage('Generate Allure Report') {
        script {
            allure includeProperties: false, jdk: '', results: [
                    [path: 'allure-results/api'],
                    [path: 'allure-results/mobile'],
                    [path: 'allure-results/web']
            ]
        }
    }

}
