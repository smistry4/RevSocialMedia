pipeline {
    agent any
    environment {
        JAVA_HOME = "/opt/jdk-21"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/smistry4/RevSocialMedia.git'
            }
        }
        stage('Build with Maven') {
            steps{
                dir('socialmedia'){
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build Docker Image') {
            steps{
                dir('socialmedia'){
                    sh '''
                    docker build -t springboot-app .
                    '''
                }
            }
        }
        stage('Deploy') {
            steps{
               sh '''
               docker stop springboot-app || true
               docker rm springboot-app || true
               docker run -d --name springboot-app \
                -p 8081:8081 springboot-app
               ''' 
            }
        }
    }
}