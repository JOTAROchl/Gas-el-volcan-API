package cl.gasvolcan.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Gas El Volcan API",
		version = "v1",
		description = "API REST para consultar y administrar el catalogo de productos."))
public class GasElVolcanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GasElVolcanApiApplication.class, args);
	}

}
