package id.mydev.auth.config;

import id.mydev.auth.utility.RestCaller;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestCallerConfiguration {
    @Qualifier("webClientBuilder")
    private final WebClient.Builder webClientBuilder;

    public RestCallerConfiguration(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    @Primary
    ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext ctx) {
        return ServiceInstanceListSupplier
                .builder()
                .withRetryAwareness()
                .withHealthChecks(webClientBuilder.build())
                .withBase(new RestCaller("restCaller"))
                .build(ctx)
                ;
    }
}
