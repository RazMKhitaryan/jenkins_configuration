node('maven') {
    env.JOBS_DIR = "${WORKSPACE}/jobs"
    env.CONFIG_FILE = "${WORKSPACE}/uploader.ini"

    stage('Start Upload') {
        echo "Starting Jenkins Job Uploader..."
    }

    stage('Checkout Repo') {
        git branch: 'main', url: 'https://github.com/RazMKhitaryan/jenkins_configuration.git'
    }

    stage('Create uploader.ini') {
        sh """
        cat > ${CONFIG_FILE} <<'EOF'
[job_builder]
recursive=True
keep_descriptions=False

[jenkins]
url=http://172.19.0.5:8080/jenkins/
user=admin
password=11bf6664b79a47770bf07d6bf18d088417
crumb=True
EOF
        """
        sh "cat ${CONFIG_FILE}"
    }

    stage('Run Upload Script') {
        sh "jenkins-jobs --conf  ${CONFIG_FILE} --flush-cache update ${JOBS_DIR}"
    }

    stage('Finish Upload') {
        echo "Upload finished successfully!"
    }
}
