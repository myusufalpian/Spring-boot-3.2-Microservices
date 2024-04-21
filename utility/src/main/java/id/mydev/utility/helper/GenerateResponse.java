package id.mydev.utility.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.mydev.utility.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GenerateResponse {

    public static ResponseEntity<?> success(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 200)), HttpStatus.OK);
    }

    public static ResponseEntity<?> created(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 201)), HttpStatus.CREATED);
    }

    public static ResponseEntity<?> error(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 500)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<?> notFound(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 404)), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> unauthorized(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 401)), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> badRequest(String message, Object data) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 400)), HttpStatus.BAD_REQUEST);
    }

}
