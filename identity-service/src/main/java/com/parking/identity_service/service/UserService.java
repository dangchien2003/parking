package com.parking.identity_service.service;

import com.parking.event.dto.EventCustomerCreate;
import com.parking.identity_service.dto.request.*;
import com.parking.identity_service.dto.response.UserResponse;
import com.parking.identity_service.entity.Role;
import com.parking.identity_service.entity.User;
import com.parking.identity_service.enums.EBlock;
import com.parking.identity_service.enums.ERole;
import com.parking.identity_service.exception.AppException;
import com.parking.identity_service.exception.ErrorCode;
import com.parking.identity_service.mapper.ProfileMapper;
import com.parking.identity_service.mapper.UserMapper;
import com.parking.identity_service.repository.RoleRepository;
import com.parking.identity_service.repository.UserRepository;
import com.parking.identity_service.repository.httpclient.ProfileClient;
import com.parking.identity_service.repository.httpclient.VaultClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserMapper userMapper;
    ProfileMapper profileMapper;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    VaultClient vaultClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PreAuthorize("hasAnyAuthority('EDIT_USER')")
    public List<UserResponse> blockAccountById(BlockUserRequest request) {

        List<User> users = userRepository.findAllById(request.getListUid());

        users.forEach(user ->
                user.setIsBlocked(EBlock.BLOCKED.getValue()));

        return userRepository.saveAll(users)
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public UserResponse createStaff(StaffCreationRequest request) {
        User user = userMapper.staffCreationRequestToUser(request);

        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add(ERole.STAFF.name());

        user = create(user, defaultRoles);

        return userMapper.toUserResponse(user);
    }

    public UserResponse createCustomer(CustomerCreationRequest request) {

        User user = userMapper.customerCreationRequestToUser(request);

        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add(ERole.CUSTOMER.name());

        user = create(user, defaultRoles);

        CustomerProfileCreationRequest profileCreationRequest = CustomerProfileCreationRequest.builder()
                .name("New user")
                .uid(user.getUid())
                .build();

        profileClient.customerCreateProfile(profileCreationRequest);


        vaultClient.createOwner(OwnerCreationRequest.builder()
                .uid(user.getUid())
                .build());

        kafkaTemplate.send("create-customer", EventCustomerCreate.builder()
                .email(user.getEmail())
                .build());

        return userMapper.toUserResponse(user);
    }

    User create(User user, Set<String> defaultRoles) {

        List<Role> roles = roleRepository.findAllById(defaultRoles);

        user.setIsBlocked(EBlock.NOT_BLOCK.getValue());
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return user;
    }
}
