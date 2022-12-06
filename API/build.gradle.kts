plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    `maven-publish`
    signing
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
        create<MavenPublication>("Maven") {
            groupId = "io.matchedup"
            version = version
            artifactId = project.name.toLowerCase()
            from(components["java"])
            pom {
                name.set("MatchedUp SDK")
                description.set("The SDK for MatchedUp")
                url.set("https://matchedup.io")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://github.com/MatchedUp/MatchedUpSdk/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("jmrapp1")
                        name.set("Jon Rapp")
                        email.set("jmrapp1270@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/MatchedUp/MatchedUpSdk.git")
                    url.set("https://github.com/MatchedUp/MatchedUpSdk")
                }
            }
        }
    }
    repositories {
        if (version.toString().endsWith("SNAPSHOT")) {
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
                name = "sonatypeReleaseRepository"
                credentials {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        } else {
            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
                name = "sonatypeSnapshotRepository"
                credentials {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["Maven"])
}