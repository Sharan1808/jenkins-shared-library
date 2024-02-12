def call() {
  node('workstation') {

      sh "find . | sed -e '1d' |xargs rm -rf"
      if(env.TAG_NAME ==~ ".*") {
          env.branchName = env.TAG_NAME
      } else {
          env.branchName = env.BRANCH_NAME
      }
      stage('Code Checkout') {
          //git branch: "${env.branchName}", url: 'https://github.com/Sharan1808/expense-backend'
          checkout scmGit(
                  branches: [[name: "${branchName}"]],
                  userRemoteConfigs: [[url: "https://github.com/Sharan1808/expense-backend"]]
          )
      }
      sh 'ls'
      stage('Compile') {}


      if(env.BRANCH_NAME == "main") {
          stage('Build') {}
      } else if(env.BRANCH_NAME ==~ "PR.*") {
          stage('Test Cases') {}
          stage('Integration Test Cases') {}
      } else if(env.TAG_NAME ==~ ".*") {
          stage('Build') {}
          stage('Release App') {}
      } else {
          stage('Test Cases') {}
      }


//      stage('Code Checkout') {}
//      stage('Compile') {}
//      stage('Test Cases') {}
//      stage('Integration Test Cases') {}
//      stage('Build') {}
//      stage('Release App') {}
  }
}