node('maven') {

    stage('Run All Tests in Parallel') {
        def apiBuild, mobileBuild, webBuild

        parallel(
                'API Tests': {
                    script {
                        apiBuild = build job: 'Api_tests', propagate: false
                    }
                },
                'Mobile Tests': {
                    script {
                        mobileBuild = build job: 'Mobile_tests', propagate: false
                    }
                },
                'Web Tests': {
                    script {
                        webBuild = build job: 'Web_tests', propagate: false
                    }
                }
        )
    }

    stage('Collect Allure Results') {
        script {
            copyArtifacts(
                    projectName: 'Api_tests',
                    selector: specific(apiBuild.number),
                    filter: 'allure-results/**',
                    target: 'allure-results/api',
                    flatten: true
            )
            copyArtifacts(
                    projectName: 'Mobile_tests',
                    selector: specific(mobileBuild.number),
                    filter: 'allure-results/**',
                    target: 'allure-results/mobile',
                    flatten: true
            )
            copyArtifacts(
                    projectName: 'Web_tests',
                    selector: specific(webBuild.number),
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
