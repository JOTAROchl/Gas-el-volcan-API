package cl.gasvolcan.api.auth;

import cl.gasvolcan.api.usuarios.Usuario;

public record UsuarioResponse(
		Long id,
		String run,
		String nombre,
		String apellidos,
		String email,
		String rol,
		String region,
		String comuna,
		String direccion) {

	public static UsuarioResponse desde(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getRun(),
				usuario.getNombre(),
				usuario.getApellidos(),
				usuario.getEmail(),
				usuario.getRol().name(),
				usuario.getRegion(),
				usuario.getComuna(),
				usuario.getDireccion());
	}
}
