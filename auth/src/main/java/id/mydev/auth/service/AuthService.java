package id.mydev.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.mydev.auth.config.JwtUtil;
import id.mydev.auth.dto.UserRequestRegisterDto;
import id.mydev.auth.dto.UserRequestSignin;
import id.mydev.auth.model.User;
import id.mydev.auth.repository.UserRepository;
import id.mydev.auth.utility.GenerateResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

}
