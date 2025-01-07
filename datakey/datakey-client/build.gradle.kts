dependencies {
    val grpcVersion = "1.58.0"

    implementation(project(":common"))
    implementation(project(":datakey:datakey-grpc"))

    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
}
