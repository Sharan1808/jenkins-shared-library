def AWS_SSM_PARAM(param_name) {
    def OUTPUT = sh ( script: "aws ssm get-parameter --name ${param_name} --with-decryption --query 'Parameter.Value' --output text", returnStdout: true).trim()
    return(OUTPUT)
}

def call() {
  node('workstation') {

      sh "find . | sed -e '1d' |xargs rm -rf"

      if(env.TAG_NAME ==~ ".*") {
          env.branch_name = "refs/tags/${env.TAG_NAME}"
      } else {
          if (env.BRANCH_NAME ==~ "PR-.*") {
              env.branch_name = "${env.CHANGE_BRANCH}"
          } else {
              env.branch_name = "${env.BRANCH_NAME}"
          }
      }

      stage('Code Checkout') {
//         // git branch: "${env.branchName}", url: 'https://github.com/Sharan1808/expense-backend'
          checkout scmGit(
                  branches: [[name: "${branch_name}"]],
                  userRemoteConfigs: [[url: "https://github.com/Sharan1808/${repo_name}"]]
          )
          sh 'cat Jenkinsfile'
      }

      if(app_type == "nodejs") {
          stage('Download Dependencies') {
              sh 'npm install'
          }
      }


      if(env.JOB_BASE_NAME ==~ "PR.*") {
          sh 'echo PR'
          stage('Test Cases') {
              //sh 'npm test'
          }

          stage('Code Quality') {
              env.SONAR_TOKEN = AWS_SSM_PARAM('sonar.token')
              wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_TOKEN}", var: 'PASSWORD']]]) {
                  sh '/tmp/sonar-scanner-5.0.0.2966-linux/bin/sonar-scanner -Dsonar.host.url=http://172.31.10.101:9000/ -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectKey=${repo_name} -Dsonar.exclusions=node_modules/**'
              }
          }

      } else if (env.BRANCH_NAME == "main") {
          sh 'echo main'
          stage('Build') {}
      } else if(env.TAG_NAME ==~ ".*") {
          sh 'echo TAG'
          stage('Build') {}
          stage('Release App') {}
      } else {
          sh 'echo branch'
          stage('Test Cases') {
              //sh 'npm test'
              //These test cases in organization is ideally written and we don't skip this step. Since it is LAB, So we are moving further ahead.

          }
      }

  }
}