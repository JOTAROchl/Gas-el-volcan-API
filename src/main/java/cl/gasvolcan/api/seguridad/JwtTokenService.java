package cl.gasvolcan.api.seguridad;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import cl.gasvolcan.api.usuarios.Usuario;

@Service
public class JwtTokenService {

	private final JwtEncoder jwtEncoder;
	private final String issuer;
	private final Duration expiration;

	public JwtTokenService(
			JwtEncoder jwtEncoder,
			@Value("${security.jwt.issuer}") String issuer,
			@Value("${security.jwt.expiration}") Duration expiration) {
		this.jwtEncoder = jwtEncoder;
		this.issuer = issuer;
		this.expiration = expiration;
	}

	public TokenEmitido emitir(Usuario usuario) {
		Instant ahora = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(issuer)
				.issuedAt(ahora)
				.expiresAt(ahora.plus(expiration))
				.subject(usuario.getEmail())
				.claim("roles", List.of(usuario.getRol().name()))
				.build();
		String token = jwtEncoder.encode(JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(),
				claims)).getTokenValue();
		return new TokenEmitido(token, expiration.toSeconds());
	}

	public record TokenEmitido(String accessToken, long expiresIn) {
	}
}
