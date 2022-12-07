plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    id("org.jetbrains.dokka") version "1.7.20"
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

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            artifact(javadocJar)
            artifact(sourcesJar)
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
    if (System.getenv("GPG_KEY") !== null) {
        val signingKey: String = System.getenv("GPG_KEY")
        val signingPassword: String = System.getenv("GPG_PASSWORD")
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["Maven"])
    }
}
