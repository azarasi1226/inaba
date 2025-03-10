package jp.inaba.apigateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    // 認証したいAPI
                    .requestMatchers("/api/baskets/**").authenticated()
                    // 上記以外はすべて認証を必要としない。
                    .anyRequest().permitAll()
            }
            .oauth2ResourceServer {
                // JWTをトークンの方式とする
                it.jwt { }
            }

        return http.build()
    }
}
