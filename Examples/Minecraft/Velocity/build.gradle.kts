plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-api:2.0.3")

    kapt("com.velocitypowered:velocity-api:3.0.1")
    compileOnly("com.velocitypowered:velocity-api:3.0.1")


    implementation(project(":API"))
}