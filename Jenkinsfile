pipeline {
    agent any

    environment {
        imagename = "fast_api_test_two"
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
                // ���� ���� 'fasttwo' �����̳� ����
                sh 'docker rm -f fasttwo'
                // ���ο� �̹����� 'fast' �����̳ʸ� ��׶��忡�� ����
                sh 'docker run -d --name fasttwo -p 8001:8001 -u root -v ${envfilepath}:/app/.env ${imagename}:${version}'
            }
        }

        // docker push
        stage('Finish') {
            steps {
                // ������ �ʴ� (dangling) �̹����� ã�� �����մϴ�.
                sh 'docker images -qf dangling=true | xargs -I{} docker rmi {}'
            }
        }
    }
}
