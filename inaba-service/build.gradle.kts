plugins {
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.20.10"

    kotlin("plugin.spring") version "2.2.0"
    kotlin("plugin.jpa") version "2.3.0"
}

dependencies {
    // project
    implementation(project(":inaba-core"))
    implementation(project(":inaba-grpc"))
    implementation(project(":inaba-message"))

    // spring bom
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        // 今回はmockkというライブラリを別で導入しているため、初期からあるmockの機能はoffに
        exclude(module = "mockito-core")
    }

    // axon bom
    implementation("org.axonframework:axon-spring-boot-starter")
    implementation("org.axonframework.extensions.kotlin:axon-kotlin")
    testImplementation("org.axonframework:axon-test")

    // other
    implementation("org.springframework.retry:spring-retry:2.0.12")
    testImplementation("com.ninja-squad:springmockk:5.0.1")
    implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
    implementation("io.axoniq.console:console-framework-client-spring-boot-starter:1.10.2")

    // jooq
    implementation("org.jooq:jooq:3.20.10")
    jooqCodegen("com.mysql:mysql-connector-j")
}

jooq {
    configuration {
        jdbc {
            driver = "com.mysql.cj.jdbc.Driver"
            url = "jdbc:mysql://localhost:3306/inaba"
            user = "root"
            password = "passw0rd"
        }
        generator {
            // Kotlinコードを生成
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.mysql.MySQLDatabase"
                inputSchema = "inaba"
                includes = ".*"
            }
            generate {
                // Null許容でないカラムに対してKotlinの非null型を使用する設定
                isKotlinNotNullRecordAttributes = true
            }
            target {
                packageName = "jp.inaba.service.infrastructure.jooq.generated"
            }
        }
    }
}

sourceSets.main {
    // jOOQのコード生成先をGradleの生成ソースディレクトリに設定
    // これにより、自動生成されたコードがコンパイル対象に含まれるようになる
    java.srcDirs("build/generated-sources/jooq")
}
