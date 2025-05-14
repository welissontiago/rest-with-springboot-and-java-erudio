package br.com.welissontiago.service;

import br.com.welissontiago.dto.v1.security.TokenDTO;
import br.com.welissontiago.dto.v1.security.UserCredentialsDTO;
import br.com.welissontiago.repository.UserRepository;
import br.com.welissontiago.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(UserCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())

        );

        var user = userRepository.findByUsername(credentials.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("username " + credentials.getUsername() + " not found");
        }

        var token = jwtTokenProvider.getToken(credentials.getUsername(),user.getRoles());

        return ResponseEntity.ok(token);
    }
}
