package com.bancoexterior.app.convenio.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.LimitesGenerales;


public interface ILimitesGeneralesServiceApirest {
	
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest) throws CustomException;
	
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest) throws CustomException;
	
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public String actualizar(LimiteRequest limiteRequest) throws CustomException;
	
	public String actualizar(LimiteRequest limiteRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public String crear(LimiteRequest limiteRequest) throws CustomException;
	
	public String crear(LimiteRequest limiteRequest, String accion, HttpServletRequest request) throws CustomException;
}
