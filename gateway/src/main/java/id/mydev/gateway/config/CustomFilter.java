package id.mydev.gateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomFilter extends AbstractGatewayFilterFactory<Object> {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Object config) {
        //Custom Pre Filter. Suppose we can extract JWT and perform Authentication
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isSecured.test(request)) {
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
            //Custom Post Filter.Suppose we can call error response handler based on error code.
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            }));
        };
    }

    public static class Config {
        // Put the configuration properties
    }

    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        System.out.println(String.valueOf(claims.get("userId")));
        exchange.getRequest().mutate()
                .header("userId", String.valueOf(claims.get("userId")))
                .build();
    }

}
