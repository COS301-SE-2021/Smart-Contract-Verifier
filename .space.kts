import space.jetbrains.api.runtime.types.structure.HostingAppSettingsStructure.environment

/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build and run tests") {


    container("maven:3.6.3-openjdk-15"){
		env["dbuser"] = Params("dbuser")
		env["dbpassword"] = Secrets("dbpassword")
    	shellScript{
        	content = """
            			cd ./Smart-Contract-Verifier-Server
						echo "pinging DB"
						ping -c 10 postgresql://db-postgresql-nyc3-98163-do-user-8880908-0.b.db.ondigitalocean.com
						echo "ending ping"
						mvn clean install
                    """
        }
    }



	container("maven:3.6.3-openjdk-15"){
		env["dbuser"] = Params("dbuser")
		env["dbpassword"] = Secrets("dbpassword")
		shellScript{
			content = """
            			cd ./Smart-Contract-Verifier-Server
						mvn clean package
						cp -r target $mountDir/share
                    """
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
