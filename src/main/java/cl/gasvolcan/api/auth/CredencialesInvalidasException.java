package cl.gasvolcan.api.auth;

public class CredencialesInvalidasException extends RuntimeException {

	public CredencialesInvalidasException() {
		super("El correo o la contraseña no son válidos.");
	}
}
