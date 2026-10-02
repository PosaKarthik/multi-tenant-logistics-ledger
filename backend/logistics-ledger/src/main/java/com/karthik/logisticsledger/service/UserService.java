package com.karthik.logisticsledger.service;

import com.karthik.logisticsledger.dto.UserRequest;
import com.karthik.logisticsledger.dto.UserResponse;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.entity.User;
import com.karthik.logisticsledger.entity.UserStatus;
import com.karthik.logisticsledger.exception.TenantNotFoundException;
import com.karthik.logisticsledger.exception.UserEmailAlreadyExistsException;
import com.karthik.logisticsledger.repository.TenantRepository;
import com.karthik.logisticsledger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse toUserResponse(User user){

        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        userResponse.setTenantId(user.getTenant().getId());

        return userResponse;
    }


    public UserResponse createUser(UserRequest userRequest,Long tenantId){

        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new UserEmailAlreadyExistsException("User email already exists: " + userRequest.getEmail());
        }

        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                () -> new TenantNotFoundException("Tenant not found with id: " +tenantId));

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setStatus(UserStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        user.setTenant(tenant);

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);

    }


}
