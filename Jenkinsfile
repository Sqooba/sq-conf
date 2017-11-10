#!groovy
@Library("jenkinsfile-lib") _

pipeline {
    agent any
    stages {
        stage('Clean & Compile') {
            steps {
                script {
                    def sbtHome = tool 'sbt 1.0'
                    sh "${sbtHome}/bin/sbt clean +package"
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    def sbtHome = tool 'sbt 1.0'
                    sh "${sbtHome}/bin/sbt test"
                }
            }
            post {
                always {
                    archive "target/**/*"
                    junit 'target/test-reports/*.xml'
                }
            }
        }

        stage('Publish') {
            steps {
                script {
                    def sbtHome = tool 'sbt 1.0'
                    sh "${sbtHome}/bin/sbt +publish"
                }
            }
        }
    }
}
