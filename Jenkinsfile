pipeline {
    agent any

    stages {
        // Gradle 빌드 스테이지: Spring Boot 프로젝트를 빌드합니다.
        stage('Gradle Build') {
            steps {
                // 'drill' 디렉터리 내에서 작업을 실행합니다.
                dir('drill') {
                    // gradlew 실행 권한 부여
                    sh 'chmod +x gradlew'
                    // gradlew를 사용해 프로젝트를 빌드하며 테스트는 제외합니다.
                    sh './gradlew clean build -x test'
                }
            }
        }

        // Docker 이미지 빌드 스테이지: Dockerfile을 기반으로 이미지를 빌드합니다.
        stage('Docker Build') {
            steps {
                dir('drill') {
                    // 이미지를 빌드합니다.
                    sh 'docker build -t drill:latest .'
                }
            }
        }

        // 배포 스테이지: 이전에 실행 중인 'back' 컨테이너를 제거하고 새로운 이미지로 컨테이너를 실행합니다.
        stage('Deploy') {
            steps {
                // 실행 중인 'back' 컨테이너 제거
                sh 'docker rm -f back'
                // 새로운 이미지로 'back' 컨테이너를 백그라운드에서 실행
                sh 'docker run -d --name back -p 8060:8060 -u root drill:latest'
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
