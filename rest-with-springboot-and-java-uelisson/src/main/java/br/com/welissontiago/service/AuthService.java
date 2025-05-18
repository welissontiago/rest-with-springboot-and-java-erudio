package br.com.welissontiago.service;

import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.dto.v1.security.TokenDTO;
import br.com.welissontiago.dto.v1.security.UserCredentialsDTO;
import br.com.welissontiago.exceptions.RequiredObjectisNullException;
import br.com.welissontiago.model.PersonModel;
import br.com.welissontiago.model.User;
import br.com.welissontiago.repository.UserRepository;
import br.com.welissontiago.security.jwt.JwtTokenProvider;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static br.com.welissontiago.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if(user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        }else{
            throw new UsernameNotFoundException("username " + username + " not found");
        }
        return ResponseEntity.ok(token);
    }

    private String generateHashedPassword(String password){
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder("", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Enconder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);
        return passwordEncoder.encode(password);
    }

    public UserCredentialsDTO create(UserCredentialsDTO user) {
        if(user == null) throw new RequiredObjectisNullException();
        logger.info("Create new user: " + user);
        var entity = new User();
        entity.setFullName(user.getFullname());
        entity.setUsername(user.getUsername());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = userRepository.save(entity);
        return new UserCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullName());
    }
}
