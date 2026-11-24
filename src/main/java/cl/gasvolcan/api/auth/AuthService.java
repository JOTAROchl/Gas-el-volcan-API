package cl.gasvolcan.api.auth;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.gasvolcan.api.seguridad.JwtTokenService;
import cl.gasvolcan.api.seguridad.JwtTokenService.TokenEmitido;
import cl.gasvolcan.api.usuarios.RolUsuario;
import cl.gasvolcan.api.usuarios.Usuario;
import cl.gasvolcan.api.usuarios.UsuarioRepository;

@Service
@Transactional
public class AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;

	public AuthService(
			UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder,
			JwtTokenService jwtTokenService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenService = jwtTokenService;
	}

	public AuthResponse registrar(RegistroRequest solicitud) {
		String run = normalizarRun(solicitud.run());
		String email = normalizarEmail(solicitud.email());

		if (!runValido(run)) {
			throw new DatosRegistroInvalidosException("El RUN ingresado no es válido.");
		}
		if (solicitud.password().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new DatosRegistroInvalidosException(
					"La contraseña no puede superar 72 bytes en UTF-8.");
		}
		if (usuarioRepository.existsByRun(run)) {
			throw new ConflictoRegistroException("El RUN ya está registrado.");
		}
		if (usuarioRepository.existsByEmailIgnoreCase(email)) {
			throw new ConflictoRegistroException("El correo ya está registrado.");
		}

		Usuario usuario = new Usuario(
				run,
				solicitud.nombre(),
				solicitud.apellidos(),
				email,
				passwordEncoder.encode(solicitud.password()),
				RolUsuario.CLIENTE,
				solicitud.region().trim(),
				solicitud.comuna().trim(),
				solicitud.direccion().trim());

		try {
			return crearRespuesta(usuarioRepository.saveAndFlush(usuario));
		} catch (DataIntegrityViolationException excepcion) {
			throw new ConflictoRegistroException(
					"El RUN o correo ya está registrado.");
		}
	}

	@Transactional(readOnly = true)
	public AuthResponse iniciarSesion(LoginRequest solicitud) {
		String email = normalizarEmail(solicitud.email());
		Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(CredencialesInvalidasException::new);

		if (!passwordEncoder.matches(solicitud.password(), usuario.getPasswordHash())) {
			throw new CredencialesInvalidasException();
		}

		return crearRespuesta(usuario);
	}

	private AuthResponse crearRespuesta(Usuario usuario) {
		TokenEmitido token = jwtTokenService.emitir(usuario);
		return new AuthResponse(
				token.accessToken(),
				"Bearer",
				token.expiresIn(),
				UsuarioResponse.desde(usuario));
	}

	private String normalizarEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	private String normalizarRun(String run) {
		return run.replace(".", "")
				.replace("-", "")
				.trim()
				.toUpperCase(Locale.ROOT);
	}

	private boolean runValido(String run) {
		if (!run.matches("^\\d{7,8}[0-9K]$")) {
			return false;
		}

		String cuerpo = run.substring(0, run.length() - 1);
		char verificador = run.charAt(run.length() - 1);
		int suma = 0;
		int multiplicador = 2;
		for (int indice = cuerpo.length() - 1; indice >= 0; indice--) {
			suma += Character.getNumericValue(cuerpo.charAt(indice)) * multiplicador;
			multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
		}

		int resultado = 11 - (suma % 11);
		char esperado = resultado == 11
				? '0'
				: resultado == 10 ? 'K' : (char) ('0' + resultado);
		return verificador == esperado;
	}
}
