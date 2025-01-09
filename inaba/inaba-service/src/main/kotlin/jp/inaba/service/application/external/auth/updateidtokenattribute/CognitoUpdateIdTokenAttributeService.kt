package jp.inaba.service.application.external.auth.updateidtokenattribute

interface CognitoUpdateIdTokenAttributeService {
    fun handle(
        emailAddress: String,
        attributeName: String,
        attributeContent: String,
    )
}
