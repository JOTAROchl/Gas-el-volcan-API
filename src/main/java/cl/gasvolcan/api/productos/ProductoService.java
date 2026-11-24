package cl.gasvolcan.api.productos;

import cl.gasvolcan.api.comun.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	@Transactional(readOnly = true)
	public List<ProductoResponse> listar() {
		return productoRepository.findAllByOrderByNombreAsc().stream()
				.map(ProductoResponse::desde)
				.toList();
	}

	@Transactional(readOnly = true)
	public ProductoResponse obtener(String codigo) {
		return ProductoResponse.desde(buscarProducto(codigo));
	}

	public ProductoResponse crear(ProductoRequest solicitud) {
		String codigo;
		do {
			codigo = "GEV" + UUID.randomUUID().toString()
					.replace("-", "")
					.toUpperCase(Locale.ROOT);
		} while (productoRepository.existsById(codigo));

		return ProductoResponse.desde(
				productoRepository.save(new Producto(codigo, solicitud)));
	}

	public ProductoResponse actualizar(String codigo, ProductoRequest solicitud) {
		Producto producto = buscarProducto(codigo);
		producto.actualizar(solicitud);
		return ProductoResponse.desde(productoRepository.save(producto));
	}

	public void eliminar(String codigo) {
		productoRepository.delete(buscarProducto(codigo));
	}

	private Producto buscarProducto(String codigo) {
		return productoRepository.findById(codigo)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"No se encontró el producto " + codigo + "."));
	}
}
