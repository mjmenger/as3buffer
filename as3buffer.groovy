// seedjob.groovy
pipelineJob('as3buffer') {
    definition {
        cps {
            sandbox(true)
            script("""

// this is an example declarative pipeline that says hello and goodbye
pipeline {
    agent any
    options { 
        disableConcurrentBuilds() 
        retry(3)
    }
    parameters {
        text(name: 'AS3_JSON', defaultValue: '', description: 'The AS3 declaration to post to the BIG-IP')
    }    
    stages {
        stage("Echo Request") {
            steps {
                echo "AS3 Declaration: " + params.AS3_JSON
            }
        }
        stage("Post the Declaration"){
            steps {
                script {
                    def response = httpRequest url: 'https://'+env.BIGIP_HOST+env.BIGIP_MGMT_ENDPOINT, 
                                            httpMode: 'POST',
                                            authentication: 'bigip-creds',
                                            validResponseCodes: '200:302',
                                            ignoreSslErrors: true,
                                            consoleLogResponseBody: true,
                                            requestBody: params.AS3_JSON
                    println('Status: '+response.status)
                    println("Content: "+response.content)
                }
            }
        }
    }
}""")
        }
    }
}