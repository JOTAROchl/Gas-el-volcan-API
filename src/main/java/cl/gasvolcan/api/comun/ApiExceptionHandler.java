package cl.gasvolcan.api.comun;

import cl.gasvolcan.api.auth.ConflictoRegistroException;
import cl.gasvolcan.api.auth.CredencialesInvalidasException;
import cl.gasvolcan.api.auth.DatosRegistroInvalidosException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<ProblemDetail> manejarNoEncontrado(
			RecursoNoEncontradoException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.NOT_FOUND,
				excepcion.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalle);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> manejarValidacion(
			MethodArgumentNotValidException excepcion) {
		List<String> errores = excepcion.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.toList();
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				"Revisa los datos enviados.");
		detalle.setProperty("errores", errores);
		return ResponseEntity.badRequest().body(detalle);
	}

	@ExceptionHandler(DatosRegistroInvalidosException.class)
	public ResponseEntity<ProblemDetail> manejarDatosRegistroInvalidos(
			DatosRegistroInvalidosException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				excepcion.getMessage());
		return ResponseEntity.badRequest().body(detalle);
	}

	@ExceptionHandler(ConflictoRegistroException.class)
	public ResponseEntity<ProblemDetail> manejarConflictoRegistro(
			ConflictoRegistroException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT,
				excepcion.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(detalle);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<ProblemDetail> manejarCredencialesInvalidas(
			CredencialesInvalidasException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.UNAUTHORIZED,
				excepcion.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detalle);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ProblemDetail> manejarConflicto(
			DataIntegrityViolationException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT,
				"No se pudo guardar por un conflicto de datos.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(detalle);
	}
}
