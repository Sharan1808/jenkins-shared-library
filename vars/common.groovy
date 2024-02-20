def codeCheckout() {
    sh 'env'
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
}