package com.bancoexterior.app.convenio.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Tasa;


public interface ITasaServiceApiRest {

	
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException;
	
	public List<Tasa> listaTasas(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException;
	
	public Tasa buscarTasa(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public String actualizar(TasaRequest tasaRequest) throws CustomException;
	
	public String actualizar(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public String crear(TasaRequest tasaRequest) throws CustomException;
	
	public String crear(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException;
}
