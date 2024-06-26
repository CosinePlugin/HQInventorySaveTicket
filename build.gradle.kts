plugins {
    kotlin("jvm") version "1.7.21"
}

group = "kr.hqservice.ticket"
version = "1.0.4"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        destinationDirectory.set(File("D:\\서버\\1.20.1 - 개발\\plugins"))
    }
}