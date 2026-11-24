package cl.gasvolcan.api.productos;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoApiTests {

	private static final String PRODUCTO_VALIDO = """
			{
			  "categoria": "Cilindros de Gas",
			  "nombre": "Cilindro GLP 11 kg",
			  "descripcion": "Cilindro para uso residencial.",
			  "unidad": "Unidad",
			  "precioResidencial": 12000,
			  "precioComercial": 11000,
			  "stock": 20,
			  "imagen": "/images/Galon11Kg.jpg"
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductoRepository productoRepository;

	@BeforeEach
	void limpiarProductos() {
		productoRepository.deleteAll();
	}

	@Test
	void creaYListaProductos() throws Exception {
		MvcResult resultadoCreacion = mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PRODUCTO_VALIDO))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.codigo").isNotEmpty())
				.andExpect(jsonPath("$.nombre").value("Cilindro GLP 11 kg"))
				.andReturn();
		String codigo = JsonPath.read(
				resultadoCreacion.getResponse().getContentAsString(),
				"$.codigo");

		mockMvc.perform(get("/api/productos/{codigo}", codigo))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.codigo").value(codigo));

		mockMvc.perform(get("/api/productos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].categoria").value("Cilindros de Gas"));
	}

	@Test
	void rechazaProductoConDatosInvalidos() throws Exception {
		mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"nombre\":\"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errores").isArray());
	}

	@Test
	void actualizaYEliminaProducto() throws Exception {
		MvcResult resultadoCreacion = mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PRODUCTO_VALIDO))
				.andExpect(status().isCreated())
				.andReturn();
		String codigo = JsonPath.read(
				resultadoCreacion.getResponse().getContentAsString(),
				"$.codigo");

		String productoActualizado = PRODUCTO_VALIDO.replace(
				"Cilindro GLP 11 kg",
				"Cilindro GLP 11 kg actualizado");
		mockMvc.perform(put("/api/productos/{codigo}", codigo)
						.contentType(MediaType.APPLICATION_JSON)
						.content(productoActualizado))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Cilindro GLP 11 kg actualizado"));

		mockMvc.perform(delete("/api/productos/{codigo}", codigo))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/productos/{codigo}", codigo))
				.andExpect(status().isNotFound());
	}
}
