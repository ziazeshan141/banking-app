pipeline {

agent any


environment {

IMAGE_NAME="bankapp"

ECR="123456789.dkr.ecr.us-east-2.amazonaws.com/bankapp"

}


stages {


stage('Checkout') {
steps {
git branch: 'main',
url: 'https://github.com/<username>/bankapp.git'
}
}


stage('Docker Build') {
steps {

sh '''
docker build -t $IMAGE_NAME:$BUILD_NUMBER .
'''

}
}


stage('Trivy Scan') {

steps {

sh '''

trivy image \
--severity HIGH,CRITICAL \
--exit-code 1 \
$IMAGE_NAME:$BUILD_NUMBER

'''

}

}


stage('Push to ECR') {

steps {

sh '''

aws ecr get-login-password \
--region us-east-2 |
docker login \
--username AWS \
--password-stdin $ECR


docker tag \
$IMAGE_NAME:$BUILD_NUMBER \
$ECR:$BUILD_NUMBER


docker push \
$ECR:$BUILD_NUMBER

'''

}

}


stage('Deploy to EKS') {

steps {

sh '''

aws eks update-kubeconfig \
--region us-east-2 \
--name eks-cluster


kubectl apply -f k8s/

'''

}

}


}

}