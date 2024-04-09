package id.mydev.gateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomFilter implements GatewayFilterFactory<Object> {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isSecured.test((org.springframework.http.server.ServerHttpRequest) request)) {
                final String token = this.getAuthHeader(request);
                try {
                    if (this.isAuthMissing(request)) {
                        return this.onError(exchange, "Auth header is missing in request", HttpStatus.UNAUTHORIZED);
                    }
                    if (jwtUtil.isInvalid(token)){
                        return this.onError(exchange, "Auth header is invalid", HttpStatus.UNAUTHORIZED);
                    }
                }catch (Exception e){
                    return this.onError(exchange, "Auth header is invalid", HttpStatus.UNAUTHORIZED);
                }
                this.populateRequestWithHeaders(exchange, token);
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {}));
        };
    }

    @Override
    public Class<Object> getConfigClass() {
        return Object.class; // Return the class type of the configuration object
    }

    private Mono<Void> onError(ServerWebExchange exchange, String onError, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Auth").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Auth");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("userId", String.valueOf(claims.get("userId")))
                .build();
    }
}
