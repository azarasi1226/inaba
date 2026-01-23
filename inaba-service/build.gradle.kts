plugins {
    // TODO:AxonFramework 4.12.3がSpring Boot 3系にまでしか対応していないので、まだ4系にしない方がいい。
    // 公式的には2026年には対応するから、その頃にまた考えよう。
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.20.10"

    kotlin("plugin.spring") version "2.3.0"
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
    jooqCodegen("org.jooq:jooq-meta-extensions:3.20.10") // DDLDatabase用
}

// =====================================================
// ======================jacoco=========================
// =====================================================
jooq {
    configuration {
        generator {
            // Kotlin用のコードを生成
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                // H2DBを利用し、atlasで管理しているschemaファイルを元にコード生成を行う
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "../database/schema.mysql.sql"
                    }
                }
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

// コンパイル前にjOOQのコード生成を実行するよう設定これにより常に最新のスキーマに基づいたコードが生成される
// 幸いDDLDatabaseを使っているため、DBサーバーへの接続は発生しない完全ローカル完結...最高すぎかよ...
tasks.named("compileKotlin") {
    dependsOn("jooqCodegen")
}

// =====================================================
// ======================統合テスト======================
// =====================================================
sourceSets {
    create("intTest") {
        kotlin {
            srcDir("src/intTest/kotlin")
        }
        resources {
            srcDir("src/intTest/resources")
        }

        // mainソースセットの出力を統合テストのコンパイルと実行のクラスパスに追加。
        // 簡単に言うとmainで書いたコードを参照できるようにするってこと
        compileClasspath += main.get().output
        runtimeClasspath += main.get().output
    }
}

// 統合テスト用の依存関係を追加する際に使用する implementation と runtimeOnly の設定を作成。
// "intTest"Implementationって、↑で作ったSourceSetの名前と同じことにすることで、Gradleが自動的に認識してくれるらしい。
// まじgradleむずいっていうかキモイね
val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}
val intTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}

// 統合テスト用依存関係
dependencies {
    intTestImplementation(platform("org.testcontainers:testcontainers-bom:2.0.3"))
    intTestImplementation("org.testcontainers:junit-jupiter")
    intTestImplementation("org.testcontainers:testcontainers-mysql")
    intTestImplementation("org.springframework.boot:spring-boot-testcontainers")
    intTestImplementation("org.springframework.boot:spring-boot-starter-test") {
        // 今回はmockkというライブラリを別で導入しているため、初期からあるmockの機能はoffに
        exclude(module = "mockito-core")
    }
    intTestImplementation("com.ninja-squad:springmockk:5.0.1")
}

// 専用タスク `integrationTest` を作成。
tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    // 一般的に統合テストは遅いため、初めに単体テストを実行して落ちたら統合テストは実施しないという方向にした方が良い
    shouldRunAfter("test")

    useJUnitPlatform()

    // 統合テストの結果を標準出力に表示する設定(成功したものpassed)のみ表示する
    testLogging {
        events("passed")
    }
}
