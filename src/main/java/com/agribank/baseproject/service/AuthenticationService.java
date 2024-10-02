package com.agribank.baseproject.service;

import com.agribank.baseproject.config.security.JwtService;
import com.agribank.baseproject.enums.ERole;
import com.agribank.baseproject.models.Role;
import com.agribank.baseproject.repository.RoleRepository;
import com.agribank.baseproject.repository.UserRepository;
import com.agribank.baseproject.request.AuthenticationRequest;
import com.agribank.baseproject.request.RegisterRequest;
import com.agribank.baseproject.response.AuthenticationResponse;
import com.agribank.baseproject.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findRoleByName(ERole.XDCB_TELLER.name());
        if (Objects.isNull(role)){
            role = new Role();
            role.setName(ERole.XDCB_TELLER.name());
            role.setId(ERole.XDCB_TELLER.getId());
            role.setDescription("teller");
        }
        roleList.add(role);
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .brcd(request.getBrcd())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleList)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
