package cl.gasvolcan.api.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "usuarios",
		uniqueConstraints = {
				@UniqueConstraint(name = "uk_usuarios_run", columnNames = "run"),
				@UniqueConstraint(name = "uk_usuarios_email", columnNames = "email")
		})
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 12)
	private String run;

	@Column(nullable = false, length = 300)
	private String nombre;

	@Column(nullable = false, length = 300)
	private String apellidos;

	@Column(nullable = false, length = 100)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RolUsuario rol;

	@Column(length = 100)
	private String region;

	@Column(length = 100)
	private String comuna;

	@Column(length = 300)
	private String direccion;

	protected Usuario() {
	}

	public Usuario(
			String run,
			String nombre,
			String apellidos,
			String email,
			String passwordHash,
			RolUsuario rol,
			String region,
			String comuna,
			String direccion) {
		this.run = run.trim();
		this.nombre = nombre.trim();
		this.apellidos = apellidos.trim();
		this.email = email.trim();
		this.passwordHash = passwordHash;
		this.rol = rol;
		this.region = region;
		this.comuna = comuna;
		this.direccion = direccion;
	}

	public Long getId() {
		return id;
	}

	public String getRun() {
		return run;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public RolUsuario getRol() {
		return rol;
	}

	public String getRegion() {
		return region;
	}

	public String getComuna() {
		return comuna;
	}

	public String getDireccion() {
		return direccion;
	}
}
