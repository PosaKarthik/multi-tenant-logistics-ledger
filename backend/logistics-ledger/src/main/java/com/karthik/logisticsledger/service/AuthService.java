package com.karthik.logisticsledger.service;


import com.karthik.logisticsledger.dto.LoginRequest;
import com.karthik.logisticsledger.dto.LoginResponse;
import com.karthik.logisticsledger.entity.User;
import com.karthik.logisticsledger.entity.UserStatus;
import com.karthik.logisticsledger.exception.InvalidCredentialsException;
import com.karthik.logisticsledger.exception.UserInactiveException;
import com.karthik.logisticsledger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginResponse loginUser(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->
                new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw  new InvalidCredentialsException("Invalid email or password");
        }

        if(user.getStatus() != UserStatus.ACTIVE){
            throw  new UserInactiveException("User account is inactive");
        }

        String token = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setAccessToken(token);
        loginResponse.setTokenType("Bearer");

        return loginResponse;


    }
}
