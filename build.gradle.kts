import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("jacoco")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    val axonVersion = "4.12.3"

    group = "inaba"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

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

    tasks.test {
        // test実行後にカバレッジ計測も行う
        finalizedBy(tasks.jacocoTestReport)
    }

    // Test時のカバレッジ測定の対象とするディレクトリの設定
    val jacocoInclude =
        listOf(
            "jp/inaba/service/application/command/**",
            "jp/inaba/service/domain/**",
        )

    tasks.jacocoTestReport {
        // カバレッジ測定前にtestの実行を行う
        dependsOn(tasks.test)
        // XMLとHTMLを出力し、そのロケーションも設定する
        reports {
            xml.required = true
            html.required = true
            csv.required = false
            html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
        }
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        // カバレッジ計測対象のフォルダを指定
                        include(jacocoInclude)
                    }
                },
            ),
        )
    }

    // カバレッジが一定値を下回った場合にビルドを失敗させる設定
    tasks.jacocoTestCoverageVerification {
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        include(jacocoInclude)
                    }
                },
            ),
        )

        violationRules {
            rule {
                element = "CLASS"
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = 0.80.toBigDecimal()
                }
            }
        }
    }

    dependencies {
        implementation(platform("org.axonframework:axon-bom:$axonVersion"))
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
        implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")
    }
}
