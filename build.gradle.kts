val rootFolder = rootProject.buildDir
allprojects {
    buildDir = (parent?.buildDir ?: rootFolder).resolve(name)
}

group = "io.matchedup"
version = "0.0.1-SNAPSHOT"