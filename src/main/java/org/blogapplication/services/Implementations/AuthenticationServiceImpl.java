package org.blogapplication.services.Implementations;

import org.blogapplication.dto.AuthenticationRequest;
import org.blogapplication.dto.AuthenticationResponse;
import org.blogapplication.dto.UserRequest;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.entity.User;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.AuthenticationService;
import org.blogapplication.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;

  @Override
  public UserResponse registerNewUser(UserRequest request) {
    User newUser = convertToEntity(request);
         newUser = userRepository.save(newUser);
         return convertToResponse(newUser);
  }

   private User convertToEntity(UserRequest request){
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private UserResponse convertToResponse(User registeredUser){
        return UserResponse.builder()
                .id(registeredUser.getId())
                .firstname((registeredUser.getFirstname()))
                .lastName(registeredUser.getLastname())
                .email(registeredUser.getEmail())
                .phoneNumber(registeredUser.getPhoneNumber())
                .build();
    }

  @Override
  public AuthenticationResponse login(AuthenticationRequest authrequest) {
    
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getEmail() ,authrequest.getPassword()));
    final UserDetails userDetails = userDetailsService.loadUserByUsername(authrequest.getEmail());
    final String jwtToken = jwtUtil.generateToken(userDetails);

    return new AuthenticationResponse(authrequest.getEmail(),jwtToken);
  }


  
}
