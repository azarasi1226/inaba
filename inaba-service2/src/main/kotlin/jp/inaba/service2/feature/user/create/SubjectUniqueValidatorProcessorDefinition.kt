package jp.inaba.service2.feature.command.user.create

import org.axonframework.extension.spring.config.ProcessorDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SubjectUniqueValidatorProcessorDefinition {
    @Bean
    fun subjectUniqueValidatorProcessor(): ProcessorDefinition =
        ProcessorDefinition
            .subscribingProcessor("subject-unique-validator")
            .assigningHandlers { it.beanType() == SubjectUniqueValidator::class.java }
            .withDefaultSettings()
}
