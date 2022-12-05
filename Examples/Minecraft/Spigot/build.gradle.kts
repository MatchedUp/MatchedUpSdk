plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly(kotlin("stdlib"))

    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    implementation(project(":API"))
}