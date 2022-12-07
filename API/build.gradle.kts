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
            println("Using SNAPSHOT version: " + version.toString())
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
                name = "sonatypeReleaseRepository"
                credentials {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        } else {
            println("Using Release version: " + version.toString())
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
    val signingKey: String? = System.getenv("GPG_KEY")
    val signingPassword: String? = System.getenv("GPG_PASSWORD")

    if (signingKey == null || signingPassword == null) {
        println("No signing credentials detected, skipping signing for published artifacts.")
    } else {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["Maven"])
    }
}
