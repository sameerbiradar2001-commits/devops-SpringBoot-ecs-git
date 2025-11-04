pipeline {
	agent any

	environment {
		AWS_REGION = 'ap-south-1'
		ECR_URL = '123456789012.dkr.ecr.ap-south-1.amazonaws.com'
		ECR_REPO = "${ECR_URL}/springboot-demo"
		ECS_CLUSTER = 'springboot-cluster'
		AWS_CREDENTIALS = 'aws-creds'
	}

	stages {
		stage('Check Branch') {
			when {
				expression { env.BRANCH_NAME != 'main' }
			}
			steps {
				echo "⏭️ Skipping build for branch: ${env.BRANCH_NAME}"
				script { currentBuild.result = 'SUCCESS' }
				error("Not main branch — skipping deployment.")
			}
		}

		stage('Checkout Code') {
			when { branch 'main' }
			steps {
				checkout scm
			}
		}

		stage('Build & Push Docker Image') {
			when { branch 'main' }
			steps {
				script {
					def imageTag = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
					withCredentials([aws(credentialsId: "${AWS_CREDENTIALS}")]) {
						sh """
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URL}
                        docker build -t ${ECR_REPO}:${imageTag} .
                        docker push ${ECR_REPO}:${imageTag}
                        """
					}
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
