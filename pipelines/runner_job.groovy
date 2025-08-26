node('maven') {
    stage('Run All Tests in Parallel') {
        parallel(
                'API Tests': {
                    build job: 'Api_tests', propagate: false
                },
                'Mobile Tests': {
                    build job: 'Mobile_tests', propagate: false
                },
                'Web Tests': {
                    build job: 'Web_tests', propagate: false
                }
        )
    }

    stage('Collect Allure Results') {
        copyArtifacts(projectName: 'Api_tests', filter: 'allure-results/**', target: 'allure-results/api', flatten: true)
        copyArtifacts(projectName: 'Mobile_tests', filter: 'allure-results/**', target: 'allure-results/mobile', flatten: true)
        copyArtifacts(projectName: 'Web_tests', filter: 'allure-results/**', target: 'allure-results/web', flatten: true)
    }

    stage('Generate Allure Report') {
        allure includeProperties: false, jdk: '', results: [
                [path: 'allure-results/api'],
                [path: 'allure-results/mobile'],
                [path: 'allure-results/web']
        ]
    }
}
