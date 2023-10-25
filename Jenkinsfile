pipeline {
    agent any

    stages {
        // Docker 이미지 빌드 스테이지: Dockerfile을 기반으로 이미지를 빌드합니다.
        stage('Docker Build') {
            steps {
                dir('Backend') {
                    sh 'docker build -t backendtest:latest .'
                }
            }
        }

        stage('Deploy') {
            steps {
                // 실행 중인 'back' 컨테이너 제거
                sh 'docker rm -f backendtest'
                // 새로운 이미지로 'back' 컨테이너를 백그라운드에서 실행
                sh 'docker run -d --name back -p 8093:8060 -u root backendtest:latest'
            }
        }

        // 완료 스테이지: 더이상 사용되지 않는 Docker 이미지를 제거합니다.
        stage('Finish') {
            steps {
                // 사용되지 않는 (dangling) 이미지를 찾아 제거합니다.
                sh 'docker images -qf dangling=true | xargs -I{} docker rmi {}'
            }
        }
    }
}
