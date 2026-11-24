package cl.gasvolcan.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión con JWT.")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/registro")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registrar una cuenta de cliente")
	public AuthResponse registrar(@Valid @RequestBody RegistroRequest solicitud) {
		return authService.registrar(solicitud);
	}

	@PostMapping("/login")
	@Operation(summary = "Iniciar sesión y obtener un JWT")
	public AuthResponse iniciarSesion(@Valid @RequestBody LoginRequest solicitud) {
		return authService.iniciarSesion(solicitud);
	}
}
