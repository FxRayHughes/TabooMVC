import io.izzel.taboolib.gradle.*

plugins {
    kotlin("jvm") version "1.9.24"
    id("io.izzel.taboolib") version "2.0.18"
    application
}

taboolib {
    description {
        contributors {
            name("枫溪")
        }
        dependencies {

        }
    }
    env {
        isDebug = false
        install(Database, DatabaseAlkaidRedis, "database-orm","database-alkaid-redis-tool")
        install(App)
        fileLibs = "libraries"
        fileAssets = "assets"
    }
    version {
        taboolib = "6.2.0-beta20"
        coroutines = "1.7.3"
        skipKotlinRelocate = true
        skipTabooLibRelocate = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    taboo("io.javalin:javalin:6.3.0")
    taboo("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    taboo("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    taboo("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
    taboo("org.slf4j:slf4j-simple:2.0.10")
//    taboo("com.alibaba.fastjson2:fastjson2-kotlin:2.0.53")
    taboo("com.squareup.okhttp3:okhttp:4.12.0")
    taboo(kotlin("reflect"))
    // JWT
    taboo("io.jsonwebtoken:jjwt-api:0.12.6")
    taboo("io.jsonwebtoken:jjwt-impl:0.12.6")
    taboo("io.jsonwebtoken:jjwt-jackson:0.12.6")

    taboo("org.connectbot:jbcrypt:1.0.2")
    taboo("com.larksuite.oapi:oapi-sdk:2.3.5")

    taboo("com.zaxxer:HikariCP:4.0.3")
    taboo("com.mysql:mysql-connector-j:8.2.0")

    // ORMLite 核心
    taboo("com.j256.ormlite:ormlite-core:6.1")
    // ORMLite JDBC 驱动
    taboo("com.j256.ormlite:ormlite-jdbc:6.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "top.maplex.taboomvc.Main")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("top.maplex.taboomvc.Main")
}
