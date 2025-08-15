#!/bin/bash
##############################################################################
# upload-all-jobs.sh
# Uploads all Jenkins jobs and views from the jobs/ folder to Jenkins
##############################################################################

# Exit immediately if any command fails
set -e

# Path to your Jenkins Job Builder configuration
CONFIG_FILE="/home/razmik/jenkins/uploader.ini"

# Path to your jobs folder (all YAML files)
JOBS_DIR="/home/razmik/jenkins/jobs"

echo "Starting upload of Jenkins jobs from $JOBS_DIR..."

# Run Jenkins Job Builder update
jenkins-jobs --conf "$CONFIG_FILE" update "$JOBS_DIR"

echo "All Jenkins jobs and views uploaded successfully!"
