val rootFolder = rootProject.buildDir

plugins {
    kotlin("jvm") version "1.6.20"
}

allprojects {
    buildDir = (parent?.buildDir ?: rootFolder).resolve(name)

    group = "io.matchedup"
    version = "0.0.5"
}
