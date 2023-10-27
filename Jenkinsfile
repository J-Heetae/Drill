pipeline {
    agent any
    
    stages {
        // Docker 이미지 빌드 스테이지: Dockerfile을 기반으로 이미지를 빌드합니다.
        stage('Docker Build') {
            steps {
                dir('drill') {
                    script {
                        // // 이미지를 빌드합니다.
                        // sh 'docker build -t drill_back:latest .'
                        def dockerImage = docker.build('drill_back:latest', '.')  // Docker 이미지 빌드
                        dockerImage.inside {  // Docker 이미지 내에서 실행 시작
                            sh './gradlew build -x test'  // 테스트 제외하고 애플리케이션 빌드
                        }  // Docker 이미지 내에서 실행 종료
                    }
                }
            }
        }

        // 배포 스테이지: 이전에 실행 중인 'drill_back' 컨테이너를 제거하고 새로운 이미지로 컨테이너를 실행합니다.
        stage('Deploy') {
            steps {
                script {  // Groovy 스크립트 블록 시작
                    // // 실행 중인 'back' 컨테이너 제거
                    // sh 'docker rm -f drill_back'
                    // // 새로운 이미지로 'drill_back' 컨테이너를 백그라운드에서 실행
                    // sh 'docker run -d --name drill_back -p 8060:8060 -u root drill_back:latest'
                    
                    def existingContainer = sh(script: 'docker ps -aqf "name=drill_back"', returnStatus: true)  // 이전 컨테이너 존재 여부 확인
                    if (existingContainer == 0) {  // 이전 컨테이너가 없으면
                        sh 'docker run -d --name drill_back -p 8060:8060 -u root drill_back:latest'  // 컨테이너 실행
                    } else {  // 이전 컨테이너가 있으면
                        sh 'docker rm -f drill_back'  // 이전 컨테이너 제거
                        sh 'docker run -d --name drill_back -p 8060:8060 -u root drill_back:latest'  // 새 컨테이너 실행
                    }
                }
            }
        }

        // 완료 스테이지: 더이상 사용되지 않는 Docker 이미지를 제거합니다.
        stage('Finish') {
            steps {
                // 사용되지 않는 (dangling) 이미지를 찾아 제거합니다.
                // sh 'docker images -qf dangling=true | xargs -I{} docker rmi {}'
                sh 'docker image prune -f'  // 사용하지 않는 Docker 이미지 정리
            }
        }
    }
}
