package br.com.welissontiago.controller;

import br.com.welissontiago.controller.docs.AuthControllerDocs;
import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.dto.v1.security.TokenDTO;
import br.com.welissontiago.dto.v1.security.UserCredentialsDTO;
import br.com.welissontiago.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authenticate endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signin(@RequestBody UserCredentialsDTO userCredentials) {
        if(credentialsInvalid(userCredentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credencials invalid");
        }
        var token = authService.signIn(userCredentials);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token invalid");
        }
        return token;
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if(parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credencials invalid");
        }
        var token = authService.refreshToken(username, refreshToken);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token invalid");
        }
        return token;
    }

    @PostMapping(value = "/createUser", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public UserCredentialsDTO create(@RequestBody UserCredentialsDTO credentials) {
        return authService.create(credentials);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialsInvalid(UserCredentialsDTO userCredentials) {
        return userCredentials == null || StringUtils.isBlank(userCredentials.getUsername()) || StringUtils.isBlank(userCredentials.getPassword());
    }



}
