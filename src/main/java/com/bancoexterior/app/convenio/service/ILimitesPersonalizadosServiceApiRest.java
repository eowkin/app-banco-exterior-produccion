package com.bancoexterior.app.convenio.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;


public interface ILimitesPersonalizadosServiceApiRest {
	
	public List<LimitesPersonalizados> listaLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest)throws CustomException;
	
	public List<LimitesPersonalizados> listaLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest)throws CustomException;
	
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest)throws CustomException;
	
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest)throws CustomException;
	
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion, HttpServletRequest request)throws CustomException;
}
