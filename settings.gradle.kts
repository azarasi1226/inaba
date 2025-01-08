rootProject.name = "inaba-root"

include(
    "common",
    "catalog:catalog-api",
    "catalog:catalog-service",
    "catalog:catalog-share",
    "identity:identity-api",
    "identity:identity-service",
    "identity:identity-share",
    "order:order-api",
    "order:order-service",
    "basket:basket-api",
    "basket:basket-service",
    "basket:basket-share",
    "datakey:datakey-service",
    "datakey:datakey-client",
    "datakey:datakey-grpc",

    "inaba:inaba-message",
    "inaba:inaba-grpc",
    "inaba:inaba-service",
    "inaba:inaba-core",
)
