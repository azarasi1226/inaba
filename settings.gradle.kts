rootProject.name = "inaba-root"

include(
    "common",
    "identity:identity-api",
    "identity:identity-service",
    "identity:identity-share",
    "order:order-api",
    "order:order-service",
    "datakey:datakey-service",
    "datakey:datakey-client",
    "datakey:datakey-grpc",

    "inaba:inaba-message",
    "inaba:inaba-grpc",
    "inaba:inaba-service",
    "inaba:inaba-core",
)
