package cl.gasvolcan.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
		@NotBlank @Size(min = 7, max = 12) String run,
		@NotBlank @Size(max = 300) String nombre,
		@NotBlank @Size(max = 300) String apellidos,
		@NotBlank @Email @Size(max = 100) String email,
		@NotBlank @Size(min = 8, max = 72)
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$") String password,
		@NotBlank @Size(max = 100) String region,
		@NotBlank @Size(max = 100) String comuna,
		@NotBlank @Size(max = 300) String direccion) {
}
