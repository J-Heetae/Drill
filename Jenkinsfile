// pipeline {
//     agent any
//     // Build stage : Multi-stage-build를 수행하여 중간 이미지 생성
//     stages {
//         stage('Build') {
//             steps {
//                 dir('drill') { // 'drill' 디렉토리로 이동
//                     script {
//                         sh 'docker build --target builder -t drill_builder:latest .' // 중간 이미지 생성
//                     }
//                 }
//             }
//         }
//         // Docker Build stage : 최종 Docker 이미지를 생성.
//         stage('Docker Build') {
//             steps {
//                 dir('drill') { // 'drill' 디렉토리로 이동
//                     script {
//                         sh 'docker build -t drill_back:latest .' // 이미지를 빌드합니다.
//                     }
//                 }
//             }
//         }
//         // Deploy stag : 이미 실행 중인 'drill_back' 컨테이너를 종료하고 새 컨테이너를 실행하여 배포.
//         stage('Deploy') {
//             steps {
//                 script {
//                     sh 'docker rm -f drill_back || true'// 실행 중인 'back' 컨테이너 제거
//                     sh 'docker run -d --name drill_back -p 8060:8060 -u root drill_back:latest' // 새로운 이미지로 'drill_back' 컨테이너를 백그라운드에서 실행
//                 }
//             }
//         }
//         // Finish stage : 사용하지 않는 Docker 이미지를 정리.
//         stage('Finish') {
//             steps {
//                 sh 'docker image prune -f'  // 사용하지 않는 Docker 이미지 정리
//             }
//         }
//     }
// }

pipeline {
    agent any

    stages {
        // Docker 이미지 빌드 스테이지: Dockerfile을 기반으로 이미지를 빌드합니다.
        stage('Docker Build') {
            steps {
                dir('drill') {
                    sh 'docker build -t drill_builder:latest .'
                }
            }
        }

        stage('Deploy') {
            steps {
                // 실행 중인 'backendtest' 컨테이너 제거
                sh 'docker rm -f springtest'
                // 새로운 이미지로 'backendtest' 컨테이너를 백그라운드에서 실행
                sh 'docker run -d --name drill_builder -p 8060:8060 -u root drill_builder:latest'
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
