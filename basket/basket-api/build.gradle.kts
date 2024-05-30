dependencies {
    implementation(project(":common"))
    implementation(project(":identity:identity-api"))
    implementation(project(":catalog:catalog-api"))

    implementation("org.axonframework:axon-modelling")
    implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")
}
