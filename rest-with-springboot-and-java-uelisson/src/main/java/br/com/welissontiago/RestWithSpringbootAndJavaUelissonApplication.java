package br.com.welissontiago;

import br.com.welissontiago.model.User;
import br.com.welissontiago.security.jwt.JwtTokenFilter;
import br.com.welissontiago.security.jwt.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RestWithSpringbootAndJavaUelissonApplication {

	private final JwtTokenProvider jwtTokenProvider;

	public RestWithSpringbootAndJavaUelissonApplication(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public static void main(String[] args) {
		SpringApplication.run(RestWithSpringbootAndJavaUelissonApplication.class, args);

		//generateHashedPassword();
	}

		private static void generateHashedPassword(){
			PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder("", 8, 185000,
					Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
			Map<String, PasswordEncoder> encoders = new HashMap<>();
			encoders.put("pbkdf2", pbkdf2Enconder);
			DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
			passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);
			var pass1 = passwordEncoder.encode("admin123");
			var pass2 = passwordEncoder.encode("admin234");

			System.out.println(pass1);
			System.out.println(pass2);
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtTokenFilter filter = new JwtTokenFilter(jwtTokenProvider);
		return http
				.httpBasic(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authorizeHttpRequests(
						authorizeRequests -> authorizeRequests
								.requestMatchers("/auth/signin", "/auth/refresh/**"
										,"/swagger-ui/**","/v3/api-docs/**","/auth/createUser").permitAll()
								.requestMatchers("/api/**").authenticated()
								.requestMatchers("/users").denyAll()
				)
				.cors(cors -> {})
				.build();
	}
}


