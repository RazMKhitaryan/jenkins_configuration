node('maven') {

    // Declare build variables
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
            // Copy artifacts only if build is not null and job actually archived results
            if (apiBuild != null) {
                copyArtifacts(
                        projectName: 'Api_tests',
                        selector: specific(apiBuild.number.toString()),
                        filter: 'target/allure-results/**',
                        target: 'allure-results/api',
                        flatten: true,
                        optional: true // <- prevents failure if no artifacts
                )
            }
            if (mobileBuild != null) {
                copyArtifacts(
                        projectName: 'Mobile_tests',
                        selector: specific(mobileBuild.number.toString()),
                        filter: 'target/allure-results/**',
                        target: 'allure-results/mobile',
                        flatten: true,
                        optional: true
                )
            }
            if (webBuild != null) {
                copyArtifacts(
                        projectName: 'Web_tests',
                        selector: specific(webBuild.number.toString()),
                        filter: 'target/allure-results/**',
                        target: 'allure-results/web',
                        flatten: true,
                        optional: true
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
