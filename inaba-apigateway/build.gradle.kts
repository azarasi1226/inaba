apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "kotlin-allopen")

dependencies {
    // project
    implementation(project(":inaba-grpc"))

    // spring bom
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }

    // other
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")
    testImplementation("com.ninja-squad:springmockk:5.0.1")
    implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
    implementation("software.amazon.awssdk:s3:2.41.6")
    implementation("org.bytedeco:javacv-platform:1.5.11")
}
