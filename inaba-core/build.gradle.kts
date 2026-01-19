dependencies {
    // JUnit 6
    implementation(platform("org.junit:junit-bom:6.0.1"))
    // JUnitのコア
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    // JUnitエンジン
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    // パラメーターテストに必要
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    // JUnit Platform launcher（テスト実行に必須）
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Mockk（Kotlin のモッキングライブラリ）
    testImplementation("io.mockk:mockk:1.14.7")
}
