package id.mydev.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.mydev.auth.config.JwtUtil;
import id.mydev.auth.dto.UserRequestRegisterDto;
import id.mydev.auth.dto.UserRequestSignin;
import id.mydev.auth.model.User;
import id.mydev.auth.repository.UserRepository;
import id.mydev.auth.utility.GenerateResponse;
import id.mydev.auth.utility.Helper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    public ResponseEntity<?> signup(UserRequestRegisterDto param) throws JsonProcessingException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        if (!param.getUserPass().equals(param.getUserConfirmPass())) return GenerateResponse.badRequest("Password and confirmation password not match", null);
        User user = mapper.map(param, User.class);
        user.setUserUuid(UUID.randomUUID().toString());
        user.setCreatedDate(new Date());
        user.setUserPass(encoder.encode(user.getUserPass()));
        user.setUserStatus(0);
        userRepository.save(user);
        return GenerateResponse.success("Add new user success", null);
    }

    public ResponseEntity<?> signin(UserRequestSignin param) throws JsonProcessingException {
        Optional<User> user = userRepository.findUserByUsernameAndUserStatus(param.getUsername(), 0);
        if (user.isEmpty()) return GenerateResponse.notFound("User not found", null);
        if (!encoder.matches(param.getPassword(), user.get().getUserPass())) return GenerateResponse.badRequest("Password not matched", null);
        return GenerateResponse.success("Add new user success", jwtUtil.generate(user.get().getUserId().toString(), "ACCESS"));
    }

    public ResponseEntity<?> testHitSignIn(UserRequestSignin param) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap params = objectMapper.readValue(objectMapper.writeValueAsString(param), HashMap.class);
        HashMap result = (HashMap) Helper.webClient("http://192.168.1.100:8080/auth/signin", params, "POST", "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQUNDRVNTIiwidXNlcklkIjoiMiIsInN1YiI6IjIiLCJpYXQiOjE3MTMyODU0NTIsImV4cCI6MTcxMzM3MTg1Mn0.DeycugnCbhlk0HE9fOi-bOlVMC1AbuEuGQ6sE3aQxgSLXZNsr5VeSqeWrp9KJoz9XMjK_MdtJBhg60KHm7DMEw").getBody();
        return GenerateResponse.success("Add new user success", result.get("data"));
    }

}
