import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"

    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
    kotlin("plugin.jpa") version "2.2.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    val axonVersion = "4.11.3"

    group = "inaba"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // @Nullableや@NonNullをkotlinのnullableなどのルールに変換する
            freeCompilerArgs.add("-Xjsr305=strict")
            // バイトコードをJVM21向けのバイトコードに変換する
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        implementation(platform("org.axonframework:axon-bom:$axonVersion"))
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
        implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")
    }
}
