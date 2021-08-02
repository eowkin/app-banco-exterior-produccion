package com.bancoexterior.app.inicio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bancoexterior.app.inicio.model.Auditoria;

public interface IAuditoriaService {
	
	public Auditoria save(String codUsuario, String opcionMenu, String accion, String codRespuesta, boolean resultado, String detalle, String ipOrigen);
	
	public Page<Auditoria> listaAuditorias(String codUsuario, String fechaDesde, String fechaHasta, Pageable pageable);
	
	public Page<Auditoria> listaAuditoriasPage(String codUsuario, String fechaDesde, String fechaHasta, int page);
	
}
