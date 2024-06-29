pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Build') {
            steps {
                git 'https://github.com/ yashpal2035//spring-petclinic.git'
                sh ' clean install'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build('spring-petclinic')
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://hub.docker.com/repository/docker/yashpal2035/spring-petclinic/general', 'dckr_pat_erk91jk4Qowr7LBJP77bsYRq1As') {
                        docker.image('spring-petclinic').push('latest')
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    kubernetesDeploy(configs: 'k8s/deployment.yaml', kubeconfigId: 'kubeconfig')
                }
            }
        }
    }
}
