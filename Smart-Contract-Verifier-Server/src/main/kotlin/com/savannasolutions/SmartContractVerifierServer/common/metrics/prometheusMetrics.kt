package com.savannasolutions.SmartContractVerifierServer.common.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ActuatorMetricsConfig {
    @Bean
    fun forcePrometheusPostProcessor(
        meterRegistryPostProcessor: BeanPostProcessor,
        registry: PrometheusMeterRegistry?
    ): InitializingBean {
        return InitializingBean {
            if (registry != null) {
                meterRegistryPostProcessor.postProcessAfterInitialization(registry, "")
                registry.config().commonTags("application", "Unison_Server")
            }
        }
    }
}