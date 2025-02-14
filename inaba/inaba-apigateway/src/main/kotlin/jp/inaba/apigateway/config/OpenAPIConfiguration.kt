package jp.inaba.apigateway.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration {
    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes("OIDC", SecurityScheme()
                        .type(SecurityScheme.Type.OPENIDCONNECT)
                        //TODO(環境変数から)
                        .openIdConnectUrl("https://cognito-idp.ap-northeast-1.amazonaws.com/ap-northeast-1_rXbtLxdoU/.well-known/openid-configuration")
                    )
            )
            .info(
                Info().title("Inaba")
                    .version("0.0.1")
            )

    }
}