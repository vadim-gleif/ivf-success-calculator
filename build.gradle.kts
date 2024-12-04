plugins {
    kotlin("jvm") version KOTLIN_VERSION
    kotlin("plugin.allopen") version KOTLIN_VERSION
    kotlin("plugin.serialization") version KOTLIN_VERSION

    id("io.quarkus") version QUARKUS_VERSION
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${QUARKUS_VERSION}"))

    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-arc")

    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-kotlin-serialization")
    implementation("io.quarkus:quarkus-logging-json")

    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.rest-assured:kotlin-extensions")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$KOTEST_VERSION")
    testImplementation("io.kotest:kotest-assertions-json-jvm:$KOTEST_VERSION")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}