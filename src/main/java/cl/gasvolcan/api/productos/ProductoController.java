package cl.gasvolcan.api.productos;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping
	public List<ProductoResponse> listar() {
		return productoService.listar();
	}

	@GetMapping("/{codigo}")
	public ProductoResponse obtener(@PathVariable String codigo) {
		return productoService.obtener(codigo);
	}

	@PostMapping
	public ResponseEntity<ProductoResponse> crear(
			@Valid @RequestBody ProductoRequest solicitud) {
		ProductoResponse producto = productoService.crear(solicitud);
		URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{codigo}")
				.buildAndExpand(producto.codigo())
				.toUri();
		return ResponseEntity.created(ubicacion).body(producto);
	}

	@PutMapping("/{codigo}")
	public ProductoResponse actualizar(
			@PathVariable String codigo,
			@Valid @RequestBody ProductoRequest solicitud) {
		return productoService.actualizar(codigo, solicitud);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
		productoService.eliminar(codigo);
		return ResponseEntity.noContent().build();
	}
}
