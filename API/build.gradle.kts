plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
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