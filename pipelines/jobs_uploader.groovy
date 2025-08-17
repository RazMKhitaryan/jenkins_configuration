pipeline {
    agent { label 'maven3' }

    stages {
        stage('Upload All Jobs') {
            steps {
                echo "Starting job uploader..."
                sh 'jenkins-jobs --conf /home/razmik/jenkins/uploader.ini update /home/razmik/jenkins/jobs'
                echo "All jobs uploaded successfully!"
            }
        }
    }
}
