/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build and run tests") {


    container("maven:3.6.3-openjdk-15-slim"){
    	shellScript{
        	content = """
            			cd ./Smart-Contract-Verifier-Server
						psql -u postgres -c 'CREATE DATABASE verifier;' 
						mvn clean install
                    """
        }
		service("postgres:latest"){
			alias("localhost")
			env["POSTGRES_PASSWORD"] = "smartcontractserverdev"
			env["POSTGRES_HOST_AUTH_METHOD"] = "trust"
		}
    }



	container("maven:3.6.3-openjdk-15-slim"){
		shellScript{
			content = """
            			cd ./Smart-Contract-Verifier-Server
						psql -u postgres -c 'CREATE DATABASE verifier;' 
						mvn clean package
						cp -r target $mountDir/share
                    """
		}
		service("postgres:latest"){
			alias("localhost")
			env["POSTGRES_PASSWORD"] = "smartcontractserverdev"
			env["POSTGRES_HOST_AUTH_METHOD"] = "trust"
		}
	}
    
    docker {
    	beforeBuildScript {
    		content = """
                 cp -r $mountDir/share docker
            """
    	}
    	build {
    		context = "docker"
    	}
    	push("savannasolutions.registry.jetbrains.space/p/scv/unison-container/myimage") {
    		tag = "\$JB_SPACE_GIT_REVISION"
    	}
        failOn {
    		timeOut {
    			timeOutInMinutes = 30
    		}
  		}
  }
}
