package com.bancoexterior.app.inicio.dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class AuditoriaDto {

	private Integer idAuditoria;
	
	private Date fecha;
	
	private String codUsuario;
	
	private String opcionMenu;
	
	private String codRespuesta;
	
	private boolean resultado;
	
	private String detalle;
	
	private String ipOrigen;
	
	private String fechaDesde;
	
	private String fechaHasta;
	
}
