pipeline {
    agent { label 'maven3' }

    environment {
        JOBS_DIR = "${WORKSPACE}/jobs"
        CONFIG_FILE = "${WORKSPACE}/uploader.ini"
    }

    stages {
        stage('Start Upload') {
            steps {
                echo "Starting Jenkins Job Uploader..."
            }
        }

        stage('Checkout Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/RazMKhitaryan/jenkins_configuration.git'
            }
        }

        stage('Create uploader.ini') {
            steps {
                script {
                    sh """
                    cat > ${CONFIG_FILE} <<'EOF'
[job_builder]
recursive=True
keep_descriptions=False

[jenkins]
url=http://172.20.0.6:8080/jenkins/
user=admin
password=11bf6664b79a47770bf07d6bf18d088417
crumb=True
EOF
                    """
                    sh "cat ${CONFIG_FILE}"
                }
            }
        }

        stage('Run Upload Script') {
            steps {
                sh "jenkins-jobs --conf ${CONFIG_FILE} --flush-cache update ${JOBS_DIR}"
            }
        }

        stage('Finish Upload') {
            steps {
                echo "Upload finished successfully!"
            }
        }
    }
}
