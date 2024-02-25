plugins {
    kotlin("jvm") version "1.7.21"
}

group = "kr.cosine.ticket"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.hqservice.kr/repository/maven-public/")
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", "1.20.2-R0.1-SNAPSHOT")

    compileOnly("kr.hqservice", "hqframework-bukkit-core", "1.0.1-SNAPSHOT")
    compileOnly("kr.hqservice", "hqframework-bukkit-command", "1.0.1-SNAPSHOT")

    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        destinationDirectory.set(File("D:\\서버\\1.20.2 - 개발\\plugins"))
    }
}