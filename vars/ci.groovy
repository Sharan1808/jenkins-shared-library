def call() {
  node('workstation') {
      sh 'env'
      stage('Code Checkout') {}
      stage('Compile') {}
      stage('Test Cases') {}
      stage('Integration Test Cases') {}
      stage('Build') {}
      stage('Release App') {}

  }
}