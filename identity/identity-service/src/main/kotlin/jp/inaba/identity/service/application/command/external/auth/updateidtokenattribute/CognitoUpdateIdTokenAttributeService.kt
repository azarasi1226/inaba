package jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute

interface CognitoUpdateIdTokenAttributeService {
    fun handle(
        emailAddress: String,
        attributeName: String,
        attributeContent: String,
    )
}
