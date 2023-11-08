pipeline {
    agent any
    // Build stage : Multi-stage-build를 수행하여 중간 이미지 생성
    stages {
        stage('Build') {
            steps {
                dir('drill') { // 'drill' 디렉토리로 이동
                    script {
                        sh 'docker build --target builder -t drill_builder:latest .' // 중간 이미지 생성
                    }
                }
            }
        }
        // Docker Build stage : 최종 Docker 이미지를 생성.
        stage('Docker Build') {
            steps {
                dir('drill') { // 'drill' 디렉토리로 이동
                    script {
                        sh 'docker build -t drill_back:latest .' // 이미지를 빌드
                    }
                }
            }
        }
        // Deploy stag : 이미 실행 중인 'drill_back' 컨테이너를 종료하고 새 컨테이너를 실행하여 배포.
        stage('Deploy') {
            steps {
                script {
                    sh 'docker rm -f drill_back_1 || true'// 실행 중인 'drill_back' 컨테이너 제거
                    sh 'docker run -d --name drill_back_1 -p 8061:8060 -u root drill_back:latest' // 새로운 이미지로 'drill_back' 컨테이너를 백그라운드에서 실행
                    sh 'docker rm -f drill_back_2 || true'
                    sh 'docker run -d --name drill_back_2 -p 8062:8060 -u root drill_back:latest'
                }
            }
        }
        // Deploy stag : 이미 실행 중인 'drill_back' 컨테이너를 종료하고 새 컨테이너를 실행하여 배포.
        stage('Deploy') {
            steps {
                script {
                    sh 'docker rm -f drill_back_1 || true'// 실행 중인 'drill_back' 컨테이너 제거
                    sh 'docker run -d --name drill_back_3 -p 8063:8060 -u root drill_back:latest' // 새로운 이미지로 'drill_back' 컨테이너를 백그라운드에서 실행
                    sh 'docker rm -f drill_back_2 || true'
                    sh 'docker run -d --name drill_back_4 -p 8064:8060 -u root drill_back:latest'
                }
            }
        }
        // Finish stage : 사용하지 않는 Docker 이미지를 정리.
        stage('Finish') {
            steps {
                sh 'docker image prune -f'  // 사용하지 않는 Docker 이미지 정리
            }
        }
    }
}
