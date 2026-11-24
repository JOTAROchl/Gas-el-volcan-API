package cl.gasvolcan.api.productos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import cl.gasvolcan.api.usuarios.RolUsuario;
import cl.gasvolcan.api.usuarios.Usuario;
import cl.gasvolcan.api.usuarios.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

	private static final String REGISTRO_VALIDO = """
			{
			  "run": "12345678-5",
			  "nombre": "Camila",
			  "apellidos": "Soto",
			  "email": "camila@ejemplo.cl",
			  "password": "Cliente1234",
			  "region": "Región Metropolitana",
			  "comuna": "Santiago",
			  "direccion": "Av. Central 123",
			  "rol": "ADMINISTRADOR"
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void limpiarProductos() {
		productoRepository.deleteAll();
		usuarioRepository.deleteAll();
	}

	@Test
	void creaYListaProductos() throws Exception {
		String token = crearAdminYObtenerToken();
		MvcResult resultadoCreacion = mockMvc.perform(post("/api/productos")
						.header("Authorization", "Bearer " + token)
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
		String token = crearAdminYObtenerToken();
		mockMvc.perform(post("/api/productos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"nombre\":\"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errores").isArray());
	}

	@Test
	void documentaOperacionesCrudEnOpenApi() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("Gas El Volcan API"))
				.andExpect(jsonPath("$.paths['/api/productos'].get").exists())
				.andExpect(jsonPath("$.paths['/api/productos'].post").exists())
				.andExpect(jsonPath("$.paths['/api/productos/{codigo}'].get").exists())
				.andExpect(jsonPath("$.paths['/api/productos/{codigo}'].put").exists())
				.andExpect(jsonPath("$.paths['/api/productos/{codigo}'].delete").exists())
				.andExpect(jsonPath("$.paths['/api/auth/registro'].post").exists())
				.andExpect(jsonPath("$.paths['/api/auth/login'].post").exists())
				.andExpect(jsonPath("$.components.securitySchemes.bearerAuth").exists());
	}

	@Test
	void persisteUsuarioConRolYContrasenaHasheada() {
		String contrasena = "Cliente1234";
		String hash = passwordEncoder.encode(contrasena);
		Usuario usuario = usuarioRepository.saveAndFlush(new Usuario(
				"12345678K",
				"Camila",
				"Soto",
				"camila@ejemplo.cl",
				hash,
				RolUsuario.CLIENTE,
				"Región Metropolitana",
				"Santiago",
				"Av. Central 123"));

		Usuario persistido = usuarioRepository
				.findByEmailIgnoreCase("CAMILA@EJEMPLO.CL")
				.orElseThrow();

		assertNotEquals(contrasena, persistido.getPasswordHash());
		assertTrue(passwordEncoder.matches(contrasena, persistido.getPasswordHash()));
		assertEquals(RolUsuario.CLIENTE, persistido.getRol());
		assertEquals(usuario.getRun(), persistido.getRun());
	}

	@Test
	void registroSiempreAsignaRolClienteYDevuelveJwt() throws Exception {
		MvcResult resultado = mockMvc.perform(post("/api/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content(REGISTRO_VALIDO))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.usuario.rol").value("CLIENTE"))
				.andExpect(jsonPath("$.usuario.passwordHash").doesNotExist())
				.andReturn();

		Usuario usuario = usuarioRepository
				.findByEmailIgnoreCase("camila@ejemplo.cl")
				.orElseThrow();
		assertNotEquals("Cliente1234", usuario.getPasswordHash());
		assertTrue(passwordEncoder.matches("Cliente1234", usuario.getPasswordHash()));
		assertEquals(RolUsuario.CLIENTE, usuario.getRol());
		assertTrue(JsonPath.read(
				resultado.getResponse().getContentAsString(),
				"$.accessToken").toString().length() > 20);
	}

	@Test
	void loginRechazaContrasenaIncorrecta() throws Exception {
		registrarCliente();

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"camila@ejemplo.cl","password":"Otra1234"}
								"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.detail").value(
						"El correo o la contraseña no son válidos."));
	}

	@Test
	void registroRechazaUnCorreoDuplicado() throws Exception {
		registrarCliente();

		mockMvc.perform(post("/api/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content(REGISTRO_VALIDO.replace(
								"12345678-5",
								"111111111")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.detail").value("El correo ya está registrado."));
	}

	@Test
	void clienteNoPuedeModificarProductos() throws Exception {
		String tokenCliente = registrarCliente();

		mockMvc.perform(post("/api/productos")
						.header("Authorization", "Bearer " + tokenCliente)
						.contentType(MediaType.APPLICATION_JSON)
						.content(PRODUCTO_VALIDO))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PRODUCTO_VALIDO))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void actualizaYEliminaProducto() throws Exception {
		String token = crearAdminYObtenerToken();
		MvcResult resultadoCreacion = mockMvc.perform(post("/api/productos")
						.header("Authorization", "Bearer " + token)
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
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(productoActualizado))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Cilindro GLP 11 kg actualizado"));

		mockMvc.perform(delete("/api/productos/{codigo}", codigo)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/productos/{codigo}", codigo))
				.andExpect(status().isNotFound());
	}

	private String crearAdminYObtenerToken() throws Exception {
		usuarioRepository.saveAndFlush(new Usuario(
				"111111111",
				"Administrador",
				"Demo",
				"admin@gasvolcan.cl",
				passwordEncoder.encode("Admin1234"),
				RolUsuario.ADMINISTRADOR,
				null,
				null,
				null));
		return iniciarSesion("admin@gasvolcan.cl", "Admin1234");
	}

	private String registrarCliente() throws Exception {
		MvcResult resultado = mockMvc.perform(post("/api/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content(REGISTRO_VALIDO))
				.andExpect(status().isCreated())
				.andReturn();
		return JsonPath.read(
				resultado.getResponse().getContentAsString(),
				"$.accessToken");
	}

	private String iniciarSesion(String email, String password) throws Exception {
		MvcResult resultado = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"%s","password":"%s"}
								""".formatted(email, password)))
				.andExpect(status().isOk())
				.andReturn();
		return JsonPath.read(
				resultado.getResponse().getContentAsString(),
				"$.accessToken");
	}
}
