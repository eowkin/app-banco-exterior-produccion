package com.bancoexterior.app.inicio.service;

import org.apache.logging.log4j.LogManager;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.inicio.model.Auditoria;
import com.bancoexterior.app.inicio.repository.IAuditoriaRepository;

@Service
public class AuditoriaServiceImpl implements IAuditoriaService{

	private static final Logger LOGGER = LogManager.getLogger(AuditoriaServiceImpl.class);
	
	@Autowired
	private IAuditoriaRepository repo;
	
	private static final String AUDITORIASERVICESAVEI = "[==== INICIO Save Auditoria - Service ====]";
	
	private static final String AUDITORIASERVICESAVEF = "[==== FIN Save Auditoria - Service ====]";
	
	private static final String AUDITORIASERVICECONSULTAI = "[==== INICIO Consulta Auditoria - Service ====]";
	
	private static final String AUDITORIASERVICECONSULTAF = "[==== FIN Consulta Auditoria - Service ====]";
	
	
	@Override
	public Auditoria save(String codUsuario, String opcionMenu, String accion, String codRespuesta, boolean resultado,
			String detalle, String ipOrigen) {
		LOGGER.info(AUDITORIASERVICESAVEI);
		Auditoria auditoria = new Auditoria();
		auditoria.setCodUsuario(codUsuario);
		auditoria.setOpcionMenu(opcionMenu);
		auditoria.setAccion(accion);
		auditoria.setCodRespuesta(codRespuesta);
		auditoria.setResultado(resultado);
		auditoria.setDetalle(detalle);
		auditoria.setIpOrigen(ipOrigen);
		LOGGER.info(AUDITORIASERVICESAVEF);
		return repo.save(auditoria);
	}


	@Override
	public Page<Auditoria> listaAuditorias(String codUsuario, String fechaDesde, String fechaHasta, Pageable pageable) {
		LOGGER.info(AUDITORIASERVICECONSULTAI);
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		LOGGER.info(AUDITORIASERVICECONSULTAF);
		return repo.listaAuditorias(codUsuario, fechaDesde, fechaHasta, pageable);
	}


	@Override
	public Page<Auditoria> listaAuditoriasPage(String codUsuario, String fechaDesde, String fechaHasta, int page) {
		LOGGER.info(AUDITORIASERVICECONSULTAI);
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		int pageNumber = page;
		int pageSize = 15;
		Sort sort = Sort.by("fecha").descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		LOGGER.info(AUDITORIASERVICECONSULTAF);
		return repo.listaAuditorias(codUsuario, fechaDesde, fechaHasta, pageable);
	}

}
