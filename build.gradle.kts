plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "io.github.zeyuyangdev.cardstaircase"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(group = "tools.aqua", name = "bgw-gui", version = "0.10")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("io.github.zeyuyangdev.cardstaircase.MainKt")

    applicationDefaultJvmArgs = listOf(
        "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
    )
}

// Configure executable fat JAR
tasks.jar {
    // specify the entry point of the application
    manifest {
        attributes["Main-Class"] = "io.github.zeyuyangdev.cardstaircase.MainKt"
    }

    // avoid duplicate resources
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // include all runtime dependencies (fat JAR)
    from({
        configurations.runtimeClasspath.get().map { file ->
            if (file.isDirectory) file else zipTree(file)
        }
    })
}