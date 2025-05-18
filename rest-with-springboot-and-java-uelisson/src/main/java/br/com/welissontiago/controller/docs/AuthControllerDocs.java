package br.com.welissontiago.controller.docs;

import br.com.welissontiago.dto.v1.security.UserCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates a user",
            description = "Authenticates a user with username and password, and returns a JWT token if successful.",
            tags = "Authentication",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> signin(@RequestBody UserCredentialsDTO userCredentials);

    @Operation(summary = "Refresh access token",
            description = "Refreshes the access token using a valid refresh token.",
            tags = "Authentication",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> refreshToken(
            @PathVariable("username") String username,
            @RequestHeader("refreshToken") String refreshToken
    );

    @Operation(summary = "Create user credentials",
            description = "Creates new user credentials (useful for initial setup or testing).",
            tags = "Authentication",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserCredentialsDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    UserCredentialsDTO create(@RequestBody UserCredentialsDTO credentials);
}
