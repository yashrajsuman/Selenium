pipeline {
  agent any
  tools { maven 'Maven3'; jdk 'JDK17' }   // names must match your Jenkins tool config
  parameters {
    string(name: 'GROUPS', defaultValue: 'smoke', description: 'JUnit tags: smoke, regression, visual')
    string(name: 'BASE_URL', defaultValue: 'https://the-internet.herokuapp.com', description: 'App under test')
  }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('UI Tests') {
      steps {
        sh "mvn -B test -Dheadless=true -Dbase.url=${params.BASE_URL} -Dgroups='${params.GROUPS}'"
      }
    }
  }
  post {
    always {
      junit 'target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/screenshots/**', allowEmptyArchive: true
    }
  }
}
