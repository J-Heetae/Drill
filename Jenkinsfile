pipeline {
    agent any
    // Build stage
    //Multi-stage-build를 수행하여 중간 이미지 생성
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
        // Docker Build stage
        //최종 Docker 이미지를 생성.
        stage('Docker Build') {
            steps {
                dir('drill') { // 'drill' 디렉토리로 이동
                    script {
                        sh 'docker build -t drill_back:latest .' // 이미지를 빌드
                    }
                }
            }
        }
        //Blue Health check
        //사용중인 경우 green
        //사용중이 아니면 blue에 신버전 배포

        //Deploy Health check
        //새로 배포한 버전의 Health check를 진행한다.
        //5초 간격으로 10번 진행
        //실패시 종료

        // Finish
        //1. nginx의 리버스 프록시 방향을 새로운 서버로 설정한다.
        //2. 기존의 서버를 종료한다.
        //3. 사용하지 않는 이미지를 삭제한다.
        stage('Blue Health check and Deploy') {
            steps {
                script {
                    sh '''
                    #!/bin/bash



                    echo "http://${ip}:${blue_port} Health check"

                    if timeout 10s curl -s "http://${ip}:${blue_port}" > /dev/null
                    then
                        echo "http://${ip}:${blue_port} Health check success"
                        target_container_name=${green_container_name}
                        target_port=${green_port}
                    else
                        echo "http://${ip}:${blue_port} Health check fail"
                        target_container_name=${blue_container_name}
                        target_port=${blue_port}
                    fi

                    docker run -d --name ${target_container_name} -p ${target_port}:8060 -u root drill_back:latest



                    echo "deploy health check"

                    for retry_count in \$(seq 60)
                    do
                    if curl -s "http://${ip}:${target_port}" > /dev/null
                    then
                        echo "Deploy Health check success"
                        break
                    fi

                    if [ $retry_count -eq 60 ]
                    then
                        echo "Deploy Health check failed"
                        exit 1
                    fi
                    sleep 2
                    done



                    echo "finish"

                    if [ $target_port -eq 8060 ]
                    then
                        echo 'set $service_port 8060;' > /etc/nginx/conf.d/service-url.inc
                    else
                        echo 'set $service_port 8061;' > /etc/nginx/conf.d/service-url.inc
                    fi

                    docker restart nginx

                    if [ "${target_port}" -eq "${blue_port}" ]
                    then
                        docker rm -f ${green_container_name} || true
                    else
                        docker rm -f ${blue_container_name} || true
                    fi

                    docker image prune -f
                    '''
                }
            }
        }
    }
    post {
        success {
            script {
                def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                mattermostSend (color: 'good', 
                message: "빌드 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)", 
                endpoint: 'https://meeting.ssafy.com/hooks/uomn1ut56tf3zfijsk84xoq9pc', 
                channel: 'A106_Back_Build'
                )
            }
        }
        failure {
            script {
                def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                mattermostSend (color: 'danger', 
                message: "빌드 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)", 
                endpoint: 'https://meeting.ssafy.com/hooks/uomn1ut56tf3zfijsk84xoq9pc', 
                channel: 'A106_Back_Build'
                )
            }
        }
    }
}
