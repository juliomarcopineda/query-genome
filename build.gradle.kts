plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.1.0" apply false
	maven
}

defaultTasks("build")

subprojects {
	apply(plugin = "java")
    apply(plugin = "maven")
	
	defaultTasks("build")
	
	repositories {
		mavenCentral()
	}
	
	dependencies {
		testImplementation("junit:junit:4.12")
    }
	
	sourceSets {
        main {
            java {
                srcDir("src/main/java")
            }
            resources {
                srcDir("src/main/java")
            }
        }
    }
}
    