package id.mydev.auth.utility;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

public class Helper {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static ResponseEntity<?> webClient(String url, HashMap<String, Object> body, String method, String token) {
        WebClient client = WebClient.builder()
                .baseUrl(url)
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", url))
                .build();

        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        WebClient.RequestBodySpec requestSpec = client.method(httpMethod);
        WebClient.RequestBodySpec headersSpec = requestSpec.headers(headers -> headers.setBearerAuth(token));

        if (body != null && !body.isEmpty()) {
            headersSpec.body(BodyInserters.fromValue(body));
        }

        return headersSpec.retrieve().toEntity(Object.class).block();
    }

}
