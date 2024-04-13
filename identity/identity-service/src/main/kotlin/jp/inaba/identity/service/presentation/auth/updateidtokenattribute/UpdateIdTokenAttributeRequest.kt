package jp.inaba.identity.service.presentation.auth.updateidtokenattribute

data class UpdateIdTokenAttributeRequest(
    val emailAddress: String,
    val idTokenAttributes: Map<String, String>
)