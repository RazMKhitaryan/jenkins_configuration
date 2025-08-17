node('maven3') {
    stage('Upload All Jobs') {
        echo "Starting job uploader..."
        sh '''
            set -ex
            jenkins-jobs --conf /home/razmik/jenkins/uploader.ini update /home/razmik/jenkins/jobs
        '''
        echo "All jobs uploaded successfully!"
    }
}
