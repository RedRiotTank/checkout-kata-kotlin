plugins {
    kotlin("jvm") version "1.9.23"
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"
val ktlintVersion = "1.2.1"

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

kotlin {
    jvmToolchain(21)
}

spotless {
    kotlin {
        ktlint(ktlintVersion)
        target("src/**/*.kt")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

tasks.test {
    useJUnitPlatform()
}