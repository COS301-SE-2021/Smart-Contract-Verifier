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
						mvn clean install
                    """
        }
    }
}
