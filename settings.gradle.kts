rootProject.name = "inaba-root"

include(
    "common",
    "datakey:datakey-service",
    "datakey:datakey-client",
    "datakey:datakey-grpc",

    "inaba:inaba-message",
    "inaba:inaba-grpc",
    "inaba:inaba-service",
    "inaba:inaba-core",
)
