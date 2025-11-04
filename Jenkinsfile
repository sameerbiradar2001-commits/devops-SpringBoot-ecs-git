pipeline {
	agent any

	environment {
		AWS_REGION = 'ap-south-1'
		ECR_URL = '123456789012.dkr.ecr.ap-south-1.amazonaws.com'   // 👈 your ECR registry URL
		ECR_REPO = "${ECR_URL}/springboot-demo"                     // 👈 your ECR repo name
		AWS_CREDENTIALS = 'aws-creds'                               // 👈 Jenkins credentials ID
	}

	stages {

		stage('Checkout Code') {
			steps {
				echo "📥 Checking out source code..."
				checkout scm
			}
		}

		stage('Build Spring Boot App') {
			steps {
				echo "🚀 Building Spring Boot app..."
				dir('demo') { // 👈 because pom.xml is inside /demo
					sh 'mvn clean package -DskipTests'
				}
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
                            docker build -t ${ECR_REPO}:${imageTag} ./demo

                            echo "📦 Pushing image to ECR..."
                            docker push ${ECR_REPO}:${imageTag}
                        """
					}
					env.IMAGE_TAG = imageTag
				}
			}
		}

		stage('Deploy Info') {
			steps {
				echo "✅ Image pushed successfully: ${ECR_REPO}:${env.IMAGE_TAG}"
				echo "🧩 Branch: ${env.BRANCH_NAME}"
			}
		}
	}

	post {
		success {
			echo "✅ Build and push successful for ${env.BRANCH_NAME}!"
		}
		failure {
			echo "❌ Build or push failed for ${env.BRANCH_NAME}!"
		}
	}
}
