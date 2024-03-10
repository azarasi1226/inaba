rootProject.name = "inaba"

include(
    "common",

    "identity:identity-api",
    "identity:identity-service",

    "order:order-api",
    "order:order-service",

    "basket:basket-api",
    "basket:basket-service"
)
