node('maven3') {
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
url=http://jenkins:8080/jenkins/
user=admin
password=11bf6664b79a47770bf07d6bf18d088417
crumb=True
EOF
        """
        sh "cat ${CONFIG_FILE}"
    }

    stage('Run Upload Script') {
        docker.image('python:3.11-slim').inside('--network selenoid_net1 -v ${WORKSPACE}:/workspace') {
            sh """
            pip install --quiet --upgrade pip
            pip install --quiet jenkins-job-builder
            jenkins-jobs --conf /workspace/uploader.ini update /workspace/jobs
            """
        }
    }

    stage('Finish Upload') {
        echo "Upload finished successfully!"
    }
}
