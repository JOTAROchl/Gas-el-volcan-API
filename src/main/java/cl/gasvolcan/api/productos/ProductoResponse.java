package cl.gasvolcan.api.productos;

import java.math.BigDecimal;

public record ProductoResponse(
		String codigo,
		String categoria,
		String nombre,
		String descripcion,
		String unidad,
		BigDecimal precioResidencial,
		BigDecimal precioComercial,
		Integer stock,
		String imagen) {

	public static ProductoResponse desde(Producto producto) {
		return new ProductoResponse(
				producto.getCodigo(),
				producto.getCategoria(),
				producto.getNombre(),
				producto.getDescripcion(),
				producto.getUnidad(),
				producto.getPrecioResidencial(),
				producto.getPrecioComercial(),
				producto.getStock(),
				producto.getImagen());
	}
}
