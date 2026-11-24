package cl.gasvolcan.api.productos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Productos", description = "Operaciones CRUD del catalogo de productos.")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping
	@Operation(summary = "Listar productos")
	@ApiResponse(responseCode = "200", description = "Lista de productos.")
	public List<ProductoResponse> listar() {
		return productoService.listar();
	}

	@GetMapping("/{codigo}")
	@Operation(summary = "Obtener un producto por codigo")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Producto encontrado."),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese codigo.")
	})
	public ProductoResponse obtener(@PathVariable String codigo) {
		return productoService.obtener(codigo);
	}

	@PostMapping
	@Operation(summary = "Crear un producto")
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Producto creado."),
			@ApiResponse(responseCode = "400", description = "Los datos enviados no son validos.")
	})
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
	@Operation(summary = "Actualizar un producto")
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Producto actualizado."),
			@ApiResponse(responseCode = "400", description = "Los datos enviados no son validos."),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese codigo.")
	})
	public ProductoResponse actualizar(
			@PathVariable String codigo,
			@Valid @RequestBody ProductoRequest solicitud) {
		return productoService.actualizar(codigo, solicitud);
	}

	@DeleteMapping("/{codigo}")
	@Operation(summary = "Eliminar un producto")
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Producto eliminado."),
			@ApiResponse(responseCode = "404", description = "No existe un producto con ese codigo.")
	})
	public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
		productoService.eliminar(codigo);
		return ResponseEntity.noContent().build();
	}
}
