def RUN_COMPILE = false
def RUN_SONAR = false
def RUN_DUP_CHECK = false
def INCLUSION_LIST = ""

pipeline {
	agent {label 'build_group'}
	stages {
		stage("Check Pull Request Title") {
			steps {
				script {
					def prTitle = env.CHANGE_TITLE
					echo prTitle
					def isValidTitle = (prTitle ==~ /(Defect|Requirement)\s(?:(,\s)?[\d]{1,5})+(?:).*/)
					echo ("Title "+isValidTitle)
					if(!isValidTitle)
						error("Please provide Defect or Req details in Pull request Title. Valid Patterns are Defect xxxxx{,yyyyy} or Requirement xxxxx{,yyyyy}");
				}
			}
		}
		stage ('Categorize') {
			steps {
				script {
					def inclusionlist = "";
					if (env.CHANGE_ID != null && env.CHANGE_TARGET != null) {
						def changedFiles = sh(returnStdout: true, script: "git diff --name-only origin/${env.CHANGE_TARGET}")
						println changedFiles
						//java file changed?
						if (changedFiles =~ /(?m)\b.*\.java$/ ){
							println "java changed"
							RUN_COMPILE = true
							RUN_SONAR = true
						}
						if (changedFiles =~ /(?m)\b.*\.jsp$/ ){
							println "jsp changed"
							RUN_COMPILE = true
						}
						//javascript changed
						if (changedFiles =~ /(?m)\b.*\.js$/ ){
							println "javascript changed"
							RUN_SONAR = true
						}
						if (changedFiles =~ /(?m)\b.*\.jar$/){
							println "jar changed"
							RUN_COMPILE = true
						}
						//spinner file changed?
						if (changedFiles =~ /(?m)\bSchema\/2018x.4_September\/DevSchema\/Spinner\/.*\.xls$/){
							println "spinner changed"
							RUN_DUP_CHECK = true
						}
						if (RUN_SONAR) {
							def javafiles = changedFiles.split('\n');
								
							boolean firstFile = true;
							for (String filename in javafiles) {
								
								if (filename.endsWith('java') ||  filename.endsWith('js')) {
									if (firstFile) {
										inclusionlist = inclusionlist + filename;
									} else {
										inclusionlist = inclusionlist + "," + filename;
									}
									firstFile = false
								}
							}
						}
						echo "inclusion list  : " + inclusionlist;
						INCLUSION_LIST = inclusionlist
					}
				}
			}
		}
		stage ('Compile'){
			steps {
				script{
					if(RUN_COMPILE){
						println "testing compile"
						step ([$class: 'CopyArtifact',
							projectName: '2018x_OOTB_FD12',
							target: 'ootb']);
						withAnt(installation: 'Ant_1.9.7') {
							sh 'ant clean retrieveOOTBSource compile dhDeployment -buildfile build.xml'
						}
					}
				}
			}
		}
		stage ('Spinner Duplicate Check'){
			steps {
				script{
					if(RUN_DUP_CHECK){
						step ([$class: 'CopyArtifact',
							projectName: 'duplicate_utility_2018x_4_September'
							]);
						sh 'javac -Xlint -cp .:Utility_Scripts/Duplicate_utility/poi-3.9.jar Utility_Scripts/Duplicate_utility/SchemaDuplicate.java'
						sh 'java -cp ./Utility_Scripts/Duplicate_utility:./Utility_Scripts/Duplicate_utility/poi-3.9.jar  SchemaDuplicate'
					}
				}
			}
		}
		stage ('SonarQube Analysis') {
			environment {
            	SONAR_SCANNER_OPTS = "-Xmx2048m"
				ANT_OPTS = "-Xmx2048m"
            } 
			when {
				changeRequest();
			}
			steps {
				script {
					if (RUN_SONAR) {
						//check to see if we have already copied artifacts
						if(!RUN_COMPILE){
							step ([$class: 'CopyArtifact', projectName: '2018x_OOTB_FD12', target: 'ootb']);
						}
						withSonarQubeEnv('sonarqube') {
							withAnt(installation: 'Ant_1.9.7') {
								sh "ant -Dsonar.inclusions=\"$INCLUSION_LIST\" sonar"
							}
						}
					}
				}
			}
		}
		stage ('SonarQube Quality Gate') {
			when {
				changeRequest();
			} //end when
			steps {
				script {
					if (RUN_SONAR) {
						timeout(time: 5, unit: 'MINUTES') {
							// Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
							// true = set pipeline to UNSTABLE, false = don't
							waitForQualityGate abortPipeline: false
						} // end timeout
					} //end if
				} //script
			} //end steps
		} // end SQQG
	}
}
