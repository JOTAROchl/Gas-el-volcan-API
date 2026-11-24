package cl.gasvolcan.api.productos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, String> {

	List<Producto> findAllByOrderByNombreAsc();
}
