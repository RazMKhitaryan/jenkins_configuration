node('maven') {

    // Declare build variables here so they are visible outside parallel
    def apiBuild = null
    def mobileBuild = null
    def webBuild = null

    stage('Run All Tests in Parallel') {
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
            // Only copy if the build ran successfully
            if (apiBuild != null) {
                copyArtifacts(
                        projectName: 'Api_tests',
                        selector: specific(apiBuild.number),
                        filter: 'allure-results/**',
                        target: 'allure-results/api',
                        flatten: true
                )
            }
            if (mobileBuild != null) {
                copyArtifacts(
                        projectName: 'Mobile_tests',
                        selector: specific(mobileBuild.number),
                        filter: 'allure-results/**',
                        target: 'allure-results/mobile',
                        flatten: true
                )
            }
            if (webBuild != null) {
                copyArtifacts(
                        projectName: 'Web_tests',
                        selector: specific(webBuild.number),
                        filter: 'allure-results/**',
                        target: 'allure-results/web',
                        flatten: true
                )
            }
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
