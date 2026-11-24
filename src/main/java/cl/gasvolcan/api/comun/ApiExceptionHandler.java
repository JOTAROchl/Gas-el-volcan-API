package cl.gasvolcan.api.comun;

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

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ProblemDetail> manejarConflicto(
			DataIntegrityViolationException excepcion) {
		ProblemDetail detalle = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT,
				"No se pudo guardar el producto por un conflicto de datos.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(detalle);
	}
}
