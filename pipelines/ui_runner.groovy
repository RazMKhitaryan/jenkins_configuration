timeout(time: 1200, unit: 'SECONDS') {
    node('maven') {
       def yamlConfig = readYaml text: $YAML_CONFIG
        stage('Create environment variables') {
            sh "echo BROWSER=${yamlConfig['BROWSER']} > .env"
            sh "echo BROWSER_VERSION=${yamlConfig['BROWSER_VERSION']} >> .env"
        }

        stage('Running UI tests') {
            sh """
                docker run --rm --name=ui_tests  \
                    --env-file ./.env \
                    --network=host \
                    -v ${pwd}/allure:/root/ui_tests/allure-results \
                    local-image
            """
        }

        post {
            always {
                stage('Allure report publisher') {
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
    }
}
