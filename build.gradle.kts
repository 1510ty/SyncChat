plugins {
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("java")
}

group = "com.mc1510ty"
version = "1.1.1"

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
    implementation("redis.clients:jedis:6.0.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {
    relocate("redis.clients", "com.mc1510ty.SyncChat.libs.redis.clients")
    archiveClassifier.set("") // これで通常の jar 名になる (例: SyncChat-1.0.0.jar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}