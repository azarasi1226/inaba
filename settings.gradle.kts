rootProject.name = "inaba"

include(
    "common",

    "catalog:catalog-api",
    "catalog:catalog-service",

    "identity:identity-api",
    "identity:identity-service",

    "order:order-api",
    "order:order-service",

    "basket:basket-api",
    "basket:basket-service"
)
