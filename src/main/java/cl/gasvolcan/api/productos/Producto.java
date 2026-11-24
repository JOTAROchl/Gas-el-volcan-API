package cl.gasvolcan.api.productos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@Column(nullable = false, length = 35)
	private String codigo;

	@Column(nullable = false, length = 80)
	private String categoria;

	@Column(nullable = false, length = 120)
	private String nombre;

	@Column(nullable = false, length = 500)
	private String descripcion;

	@Column(nullable = false, length = 30)
	private String unidad;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal precioResidencial;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal precioComercial;

	@Column(nullable = false)
	private Integer stock;

	@Column(nullable = false, length = 500)
	private String imagen;

	protected Producto() {
	}

	public Producto(String codigo, ProductoRequest solicitud) {
		this.codigo = codigo;
		actualizar(solicitud);
	}

	public void actualizar(ProductoRequest solicitud) {
		categoria = solicitud.categoria().trim();
		nombre = solicitud.nombre().trim();
		descripcion = solicitud.descripcion().trim();
		unidad = solicitud.unidad().trim();
		precioResidencial = solicitud.precioResidencial();
		precioComercial = solicitud.precioComercial();
		stock = solicitud.stock();
		imagen = solicitud.imagen().trim();
	}

	public String getCodigo() {
		return codigo;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getUnidad() {
		return unidad;
	}

	public BigDecimal getPrecioResidencial() {
		return precioResidencial;
	}

	public BigDecimal getPrecioComercial() {
		return precioComercial;
	}

	public Integer getStock() {
		return stock;
	}

	public String getImagen() {
		return imagen;
	}
}
