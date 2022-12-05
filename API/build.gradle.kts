plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))

    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.matchedup"
            version = version
            artifactId = project.name.toLowerCase()
            from(components["java"])
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/MatchedUp/MatchedUpSdk")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}