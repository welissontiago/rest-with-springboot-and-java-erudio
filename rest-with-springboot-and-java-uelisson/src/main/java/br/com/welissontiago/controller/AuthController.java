package br.com.welissontiago.controller;

import br.com.welissontiago.dto.v1.security.TokenDTO;
import br.com.welissontiago.dto.v1.security.UserCredentialsDTO;
import br.com.welissontiago.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authenticate endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserCredentialsDTO userCredentials) {
        if(credentialsInvalid(userCredentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credencials invalid");
        }
        var token = authService.signIn(userCredentials);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token invalid");
        }
        return ResponseEntity.ok().body(token);
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping("/refresh/{username}")
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken ) {
        if(parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credencials invalid");
        }
        var token = authService.refreshToken(username, refreshToken);
        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token invalid");
        }
        return ResponseEntity.ok().body(token);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialsInvalid(UserCredentialsDTO userCredentials) {
        return userCredentials == null || StringUtils.isBlank(userCredentials.getUsername()) || StringUtils.isBlank(userCredentials.getPassword());
    }



}
