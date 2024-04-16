package id.mydev.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@LoadBalancerClient(name = "restCaller", configuration = RestCallerConfiguration.class)
public class WebClientConfig {
    @LoadBalanced
    @Bean
    @Qualifier("loadBalancedWebClientBuilder")
    WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("webClientBuilder")
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
