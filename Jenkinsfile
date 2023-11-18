pipeline {
    agent any

    environment {
        imagename = "fast_api_test_three"
        registryCredential = 'admin'
        version = 'latest'
        dockerImage = ''
        envfilepath = '/home/ubuntu/.env'
    }

    stages {

        // docker build
        stage('Bulid Docker') {
          agent any
          steps {
            echo 'Bulid Docker'
            script {
                dockerImage = sh 'docker build -t ${imagename}:${version} ./Fastapi_app'
            }
          }
        }

        // docker Deploy
        stage('Deploy') {
            steps {
                sh 'docker rm -f fastthree'
                sh 'docker run -d --name fastthree -p 8002:8002 -u root -v ${envfilepath}:/app/.env -v /home/ubuntu/video:/app/src/video -v /home/ubuntu/thumbnails:/app/thumbnails ${imagename}:${version}'
            }
        }

        // docker push
        stage('Finish') {
            steps {
                sh 'docker images -qf dangling=true | xargs -I{} docker rmi {}'
            }
        }
    }
}
