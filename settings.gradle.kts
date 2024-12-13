rootProject.name = "inaba"

include(
    "common",
    "catalog:catalog-api",
    "catalog:catalog-service",
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
)
