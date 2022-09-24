plugins {
    java
}

group = "dev.racci"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of("17"))

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
}
