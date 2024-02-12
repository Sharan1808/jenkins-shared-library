def call() {
  node('workstation') {
      if(env.TAG_NAME ==~ ".*") {
          env.branchName = env.TAG_NAME
      } else {
          env.branchName = env.BRANCH_NAME
      }
      stage('Code Checkout') {
          git branch: env.branchName, url: 'https://github.com/Sharan1808/expense-backend'
      }
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