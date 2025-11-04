pipeline{
	agent anyOf

	environment {
		AWS_REGION = 'ap-south-1'
		ECR_URL = '123456789012.dkr.ecr.ap-south-1.amazonaws.com'   // 👈 your ECR registry URL
		ECR_REPO = "${ECR_URL}/springboot-demo"                     // 👈 your ECR repo name
		ECS_CLUSTER = 'springboot-cluster'                          // 👈 your ECS cluster name
		AWS_CREDENTIALS = 'aws-creds'                               // 👈 Jenkins credentials ID
	}

	stages {

		stage('Checkout Code') {
			steps {
				checkout scm
			}
		}

		stage('Build Application') {
			steps {
				echo "🚀 Building Spring Boot app..."
				sh 'mvn clean package -DskipTests'
			}
		}

		stage('Build & Push Docker Image') {
			steps {
				script {
					def imageTag = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
					withCredentials([aws(credentialsId: "${AWS_CREDENTIALS}")]) {
						sh """
              echo "🔐 Logging into Amazon ECR..."
              aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URL}

              echo "🐳 Building Docker image..."
              docker build -t ${ECR_REPO}:${imageTag} .

              echo "📦 Pushing image to ECR..."
              docker push ${ECR_REPO}:${imageTag}
            """
					}
					env.IMAGE_TAG = imageTag
				}
			}
		}



	}


	post {
		success {
			echo "✅ Build and deployment successful for ${env.BRANCH_NAME}!"
		}
		failure {
			echo "❌ Build or deployment failed for ${env.BRANCH_NAME}!"
		}
	}
}