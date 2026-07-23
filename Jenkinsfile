stage('Verify Tools') {
    steps {
        sh 'java -version'
        sh 'mvn -version'
    }
}

pipeline {
    agent any
    }

    environment {
        AWS_REGION = 'us-east-2'
        ECR_REGISTRY = '047385030300.dkr.ecr.us-east-2.amazonaws.com'
        BACKEND_IMAGE = "${ECR_REGISTRY}/banking-backend:latest"
        FRONTEND_IMAGE = "${ECR_REGISTRY}/banking-frontend:latest"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/ziazeshan141/banking-app.git'
            }
        }

        stage('Backend Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('SonarQube Scan') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=banking-app \
                        -Dsonar.host.url=18.118.146.255:9000 \
                        -Dsonar.login=$SONAR_AUTH_TOKEN
                        '''
                    }
                }
            }
        }

        stage('Trivy File Scan') {
            steps {
                sh 'trivy fs .'
            }
        }

        stage('Build Docker Images') {
            steps {
                dir('backend') {
                    sh 'docker build -t $BACKEND_IMAGE .'
                }

                dir('frontend') {
                    sh 'docker build -t $FRONTEND_IMAGE .'
                }
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh 'trivy image $BACKEND_IMAGE'
                sh 'trivy image $FRONTEND_IMAGE'
            }
        }

        stage('Push Images') {
            steps {
                sh '''
                aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY

                docker push $BACKEND_IMAGE
                docker push $FRONTEND_IMAGE
                '''
            }
        }

        stage('Deploy to EKS') {
            steps {
                sh '''
                kubectl rollout restart deployment banking-backend -n banking
                kubectl rollout restart deployment banking-frontend -n banking
                '''
            }
        }

    }
}
