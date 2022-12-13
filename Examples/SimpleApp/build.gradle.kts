plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-api:2.0.3")

    implementation(project(":API"))
}