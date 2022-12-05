val rootFolder = rootProject.buildDir

group = "io.matchedup"
version = "0.0.1-SNAPSHOT"

allprojects {
    buildDir = (parent?.buildDir ?: rootFolder).resolve(name)
}
