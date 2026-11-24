package cl.gasvolcan.api.productos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
		@NotBlank @Size(max = 80) String categoria,
		@NotBlank @Size(max = 120) String nombre,
		@NotBlank @Size(max = 500) String descripcion,
		@NotBlank @Size(max = 30) String unidad,
		@NotNull @Positive BigDecimal precioResidencial,
		@NotNull @Positive BigDecimal precioComercial,
		@NotNull @PositiveOrZero Integer stock,
		@NotBlank @Size(max = 500) String imagen) {
}
