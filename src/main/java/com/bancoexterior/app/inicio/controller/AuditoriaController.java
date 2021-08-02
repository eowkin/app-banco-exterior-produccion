package com.bancoexterior.app.inicio.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bancoexterior.app.inicio.dto.AuditoriaDto;
import com.bancoexterior.app.inicio.model.Auditoria;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/auditoria")
public class AuditoriaController {
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	private static final Logger LOGGER = LogManager.getLogger(AuditoriaController.class);
	
	private static final String URLFORMAUDITORIACONSULTA = "monitorFinanciero/auditoria/formAuditoriaConsulta";
	
	private static final String URLLISTAAUDITORIA = "monitorFinanciero/auditoria/listaAuditorias";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJENORESULTADO = "La consulta no arrojo resultado.";
	
	private static final String LISTAAUDITORIAS = "listaAuditorias";
	
	private static final String AUDITORIACONTROLLERFORMAUDITORIACONSULTAI = "[==== INICIO Form Auditoria Consulta Auditoria - Controller ====]";
	
	private static final String AUDITORIACONTROLLERFORMAUDITORIACONSULTAF = "[==== FIN Form Auditoria Consulta Auditoria - Controller ====]";
	
	@GetMapping("/formAuditoriaConsulta")
	public String formAuditoriaBuscar(AuditoriaDto auditoriaDto, Model model) {
		LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAI);
		LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAF);
		return URLFORMAUDITORIACONSULTA;
	}
	
	
	@GetMapping("/consultarAuditoria")
	public String consultarAuditoriaPrueba(AuditoriaDto auditoriaDto, Model model) {
		LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAI);
		LOGGER.info(auditoriaDto.getCodUsuario());
		LOGGER.info(auditoriaDto.getFechaDesde());
		LOGGER.info(auditoriaDto.getFechaHasta());
		
		
		
		
		List<String> listaError = new ArrayList<>();
		Page<Auditoria> listaAuditorias;
		if(libreriaUtil.isFechaValidaDesdeHasta(auditoriaDto.getFechaDesde(), auditoriaDto.getFechaHasta())){
			listaAuditorias = auditoriaService.listaAuditoriasPage( auditoriaDto.getCodUsuario(),auditoriaDto.getFechaDesde(), auditoriaDto.getFechaHasta(), 0);
			
			if(listaAuditorias.isEmpty()) {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMAUDITORIACONSULTA;
			}
			
			model.addAttribute(LISTAAUDITORIAS, listaAuditorias);
			model.addAttribute("codUsuario", auditoriaDto.getCodUsuario());
			model.addAttribute("fechaDesde", auditoriaDto.getFechaDesde());
			model.addAttribute("fechaHasta", auditoriaDto.getFechaHasta());
			LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAF);
			return URLLISTAAUDITORIA;
			
		}else {
			LOGGER.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMAUDITORIACONSULTA;
		}
		
		
		
	}
	
	
	@GetMapping("/consultarAuditoriaPage")
	public String consultarAuditoriaPagePrueba(@RequestParam("codUsuario") String codUsuario, @RequestParam("fechaDesde") String fechaDesde, 
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, AuditoriaDto auditoriaDto, Model model) {
		LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAI);
		
		List<String> listaError = new ArrayList<>();
		Page<Auditoria> listaAuditorias;
		if(libreriaUtil.isFechaValidaDesdeHasta(fechaDesde, fechaHasta)){
			listaAuditorias = auditoriaService.listaAuditoriasPage( codUsuario, fechaDesde, fechaHasta, page);
			
			if(listaAuditorias.isEmpty()) {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMAUDITORIACONSULTA;
			}
			
			model.addAttribute(LISTAAUDITORIAS, listaAuditorias);
			model.addAttribute("codUsuario", codUsuario);
			model.addAttribute("fechaDesde", fechaDesde);
			model.addAttribute("fechaHasta", fechaHasta);
			LOGGER.info(AUDITORIACONTROLLERFORMAUDITORIACONSULTAF);
			return URLLISTAAUDITORIA;
			
		}else {
			LOGGER.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMAUDITORIACONSULTA;
		}
		
		
		
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "auditoria";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}

	
}
