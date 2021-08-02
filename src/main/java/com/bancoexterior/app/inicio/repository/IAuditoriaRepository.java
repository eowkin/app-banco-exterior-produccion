package com.bancoexterior.app.inicio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.inicio.model.Auditoria;

@Repository
public interface IAuditoriaRepository extends JpaRepository<Auditoria, Integer>{

	public static final String SELECTCAUDITORIAFECHAS = "SELECT id_auditoria, fecha, cod_usuario, opcion_menu, accion, cod_respuesta, resultado, detalle, ip_origen\r\n"
			+ "FROM monitor_financiero.auditoria\r\n"
			+ "where cod_usuario = (case when ?1 = '' then cod_usuario else ?1 end)\r\n"
			+ "and fecha between to_timestamp(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS')";
	
	public static final String SELECTCAUDITORIAFECHASCONT = "SELECT  count(cod_usuario = (case when ?1 = '' then cod_usuario else ?1 end)\r\n"
			+ "and fecha between to_timestamp(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS'))\r\n"
			+ "FROM monitor_financiero.auditoria\r\n"
			+ "where cod_usuario = (case when ?1 = '' then cod_usuario else ?1 end)\r\n"
			+ "and fecha between to_timestamp(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS')";

	
	
	@Query(value = SELECTCAUDITORIAFECHAS,
		    countQuery = SELECTCAUDITORIAFECHASCONT,
		    nativeQuery = true)
	public Page<Auditoria> listaAuditorias(String codUsuario, String fechaDesde, String fechaHasta, Pageable pageable);
}
