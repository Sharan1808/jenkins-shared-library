def call() {
  node('workstation') {
        stage('Compile') {}
        stage('Test Cases') {}
        stage('Integration Test Cases') {}
        stage('Build') {}
        stage('Release App') {}

    }
}