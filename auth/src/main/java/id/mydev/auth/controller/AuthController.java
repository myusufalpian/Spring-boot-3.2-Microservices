package id.mydev.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.mydev.auth.dto.UserRequestRegisterDto;
import id.mydev.auth.dto.UserRequestSignin;
import id.mydev.auth.service.AuthService;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin (@RequestBody UserRequestSignin param) throws JsonProcessingException {
        return authService.signin(param);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup (@RequestBody UserRequestRegisterDto param) throws JsonProcessingException {
        return authService.signup(param);
    }

    @GetMapping(value = "/test")
    public String testController() {
        return "Welcome to auth server";
    }
}
