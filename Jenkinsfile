pipeline {
  	agent {
    	label "default"
 	}
  	//agent any
    stages  {
        
        stage("检出") {
            steps {
                sh 'ci-init'
                checkout(
                  [$class: 'GitSCM', branches: [[name: env.GIT_BUILD_REF]], 
                  userRemoteConfigs: [[url: env.GIT_REPO_URL]]]
                )
            }
        }
		
      	stage("Android SDK") {
            steps {
                sh '''
rm -rf sdk-tools-linux-4333796.* tools
wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip
unzip sdk-tools-linux-4333796.zip
         		'''
              	
              	//sh 'rm -rf platforms platform-tools'
              	sh 'yes|tools/bin/sdkmanager --licenses'
              	//sh 'yes|tools/bin/sdkmanager "platform-tools" "platforms;android-26"'	// andorid 8.0
              	//sh 'yes|tools/bin/sdkmanager "platform-tools" "platforms;android-27"' 	// andorid 8.1
              	sh 'yes|tools/bin/sdkmanager "platform-tools" "platforms;android-28"'	// andorid 9.0
              	sh '(while sleep 3; do echo "y"; done) | tools/android update sdk -u'
              
              	//writeFile(file: 'platforms/licenses/android-sdk-license', text: '''
//8933bad161af4178b1185d1a37fbf41ea5269c55
//d56f5187479451eabf01fb78af6dfcb131a6481e
//24333f8a63b6825ea9c5514f83c2829b004d1fee
 				//''')

              	sh 'ls -la ~'
              	sh 'ls -1 platforms'
              	
              	//sh 'cat ./platforms/licenses/android-sdk-license'
              	sh 'tools/bin/sdkmanager --list'
            }
        }
      
        stage("构建") {
            steps {
              	echo "构建中..."
                sh './gradlew'
              	echo "构建完成."
            }
        }
        stage("测试") {
            steps {
                echo "单元测试中..."
                sh './gradlew test'
                echo "单元测试完成."
                //junit 'app/build/test-results/*/*.xml' // 收集单元测试报告的调用过程
            }
        }      
      	stage("打包") {
            steps {
                
                sh './gradlew assemble'
                
                archiveArtifacts artifacts: 'app/build/outputs/apk/*/*.apk', fingerprint: true // 收集构建产物
            }
        }

        stage("部署") {
            steps {
                echo "部署中..."
                // 请在这里放置收集单元测试报告的调用过程，例如:
                // sh 'mvn tomcat7:deploy' // Maven tomcat7 插件示例：
                // sh './deploy.sh' // 自研部署脚本
                echo "部署完成"
            }
        }
    }
}