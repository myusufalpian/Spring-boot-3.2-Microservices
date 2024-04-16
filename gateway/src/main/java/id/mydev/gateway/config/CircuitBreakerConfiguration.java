package id.mydev.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class CircuitBreakerConfiguration {
    @Bean
    @Primary
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // Threshold kegagalan (dalam persentase) sebelum circuit breaker terbuka
                .slidingWindowSize(5) // Jumlah percobaan sebelum decision dibuat
                .permittedNumberOfCallsInHalfOpenState(3) // Jumlah pemanggilan yang diizinkan dalam mode setengah terbuka
                .minimumNumberOfCalls(10) // Jumlah pemanggilan minimum sebelum circuit breaker memutuskan apakah akan aktif atau tidak
                .waitDurationInOpenState(Duration.ofMillis(10000)) // Durasi tunggu sebelum mencoba membuka circuit breaker lagi (timeout)
                .recordExceptions(IOException.class, TimeoutException.class) // Exception yang akan dicatat sebagai kegagalan
                .build();
    }
}
