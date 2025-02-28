package jp.inaba.apigateway.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration {
    @Bean
    fun openApi(
        @Value("\${OIDC_ISSUER}") oidcIssuer: String,
    ): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        "OIDC",
                        SecurityScheme()
                            .type(SecurityScheme.Type.OPENIDCONNECT)
                            .openIdConnectUrl("$oidcIssuer/.well-known/openid-configuration"),
                    ),
            )
            .info(
                Info().title("Inaba")
                    .version("0.0.1"),
            )
    }
}
