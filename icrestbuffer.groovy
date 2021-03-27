// seedjob.groovy
pipelineJob('icrestbuffer') {
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
        string(name: 'ICREST_URI', defaultValue: '', description: 'Provide the iControlREST endpoint.')
        text(name: 'ICREST_JSON', defaultValue: '', description: 'The json payload to post to the BIG-IP')
    }    
    stages {
        stage("Echo Request") {
            steps {
                echo "JSON Declaration: " + params.ICREST_JSON
            }
        }
        stage("Post the Declaration"){
            steps {
                script {
                    def response = httpRequest url: 'https://'+env.BIGIP_HOST+params.ICREST_URI, 
                                            httpMode: 'PATCH',
                                            authentication: 'bigip-creds',
                                            validResponseCodes: '200:302',
                                            ignoreSslErrors: true,
                                            consoleLogResponseBody: true,
                                            requestBody: params.ICREST_JSON
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