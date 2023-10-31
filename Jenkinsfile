pipeline {
    agent any

    environment {
        imagename = "fast_api_test"
        registryCredential = 'admin'
        version = 'latest'
        dockerImage = ''
        envfilepath = env.ENV_FILE_PATH
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
                // ���� ���� 'back' �����̳� ����
                sh 'docker rm -f fast'
                // ���ο� �̹����� 'fast' �����̳ʸ� ��׶��忡�� ����
                sh 'docker run -d --name fast -p 8000:8000 -u root -v ${envfilepath}:/app/.env ${imagename}:${version}'
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
