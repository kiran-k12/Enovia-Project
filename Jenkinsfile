pipeline{
	agent any
	
stages{
	
	stage('SonarQube analysis') {
      steps {
        
           withMaven(maven : 'maven_3_5_0') {
        withSonarQubeEnv('sonar') {
          sh 'mvn clean package sonar:sonar'
        }
      }
    }
           }
	
}
}


