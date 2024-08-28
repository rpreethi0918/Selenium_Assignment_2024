pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Clone the repository from GitHub
                git url: 'https://github.com/rpreethi0918/Selenium_Assignment_2024', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                // Build the project using Maven
                sh 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                // Run the Selenium tests using Maven
                sh 'mvn test'
            }
        }

        stage('Post-build Actions') {
            steps {
                // Archive the test results and any important artifacts
                junit 'target/surefire-reports/*.xml'  // Adjust this path based on where your test reports are located

                // Optional: Archive other artifacts, such as jar files or logs
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            // Actions to always perform, like cleanup
            echo 'Pipeline completed.'
        }
        success {
            // Actions to take if the build is successful
            echo 'Build succeeded!'
        }
        failure {
            // Actions to take if the build fails
            echo 'Build failed!'
        }
    }
}

