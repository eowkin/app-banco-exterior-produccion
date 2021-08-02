package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;




@Service
public class LimitesPersonalizadosServiceApiRestImpl implements ILimitesPersonalizadosServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(LimitesPersonalizadosServiceApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".limitesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".limitesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio limites personalizados";
    
    private static final String LIMITESPERSONALIZADOSSERVICELISTAI = "[==== INICIO Lista LimitesPersonalizados Consultas - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICELISTAF = "[==== FIN Lista LimitesPersonalizados Consultas - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICEBUSCARI = "[==== INICIO Buscar LimitesPersonalizados Consultas - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICEBUSCARF = "[==== FIN Buscar LimitesPersonalizados Consultas - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICEACTUALIZARI = "[==== INICIO Actualizar LimitesPersonalizados - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICEACTUALIZARF = "[==== FIN Actualizar LimitesPersonalizados - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICECREARI = "[==== INICIO Crear LimitesPersonalizados - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSSERVICECREARF = "[==== FIN Crear LimitesPersonalizados - Service ====]";
	
	private static final String LIMITESPERSONALIZADOSFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  LimitesPersonalizados - Funcion-Service ====]";

	private static final String LIMITESPERSONALIZADOSFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  LimitesPersonalizados - Funcion-Service ====]";
	
	private static final String CLIENTESPERSONALIZADOS = "clientesPersonalizados";
	
	private static final String EDITLIMITECLIENTE = "editLimiteCliente";
	
	private static final String GUARDARLIMITECLIENTE = "guardarLimiteCliente";
	
	private static final String SAVELIMITECLIENTE = "saveLimiteCliente";

    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

    
	@Override
	public List<LimitesPersonalizados> listaLimitesPersonalizados(
			LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		
		LOGGER.info(LIMITESPERSONALIZADOSSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICELISTAF);
				return respuesta2xxListaLimitesPersonalizados(retorno);
			}else {
				LOGGER.error(respuesta4xxListaLimitesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaLimitesPersonalizados(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesPersonalizados> respuesta2xxListaLimitesPersonalizados(WSResponse retorno){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			return limiteResponse.getLimitesPersonalizados();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
        
	}

	public String respuesta4xxListaLimitesPersonalizados(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest)
			throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICEBUSCARF);
				return respuesta2xxBuscarLimitesPersonalizados(retorno);
			}else {
				LOGGER.error(respuesta4xxListaLimitesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaLimitesPersonalizados(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public LimitesPersonalizados respuesta2xxBuscarLimitesPersonalizados(WSResponse retorno){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimitesPersonalizados().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}
	
	@Override
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAI);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaActivarDesactivarLimites(String accion, boolean resultado, String codRespuesta, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" LimitesPersonalizados:[codigoIbs="+limitesPersonalizadosRequest.getLimiteCliente().getCodigoIbs()+"], [codMoneda="+limitesPersonalizadosRequest.getLimiteCliente().getCodMoneda()+"], [tipoTransaccion="+limitesPersonalizadosRequest.getLimiteCliente().getTipoTransaccion()+"]", request.getRemoteAddr());
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaEditLimites(String accion, boolean resultado, String codRespuesta, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAI);
			if(accion.equals(EDITLIMITECLIENTE)) {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" LimitesPersonalizados:[codigoIbs="+limitesPersonalizadosRequest.getLimiteCliente().getCodigoIbs()+"], [codMoneda="+limitesPersonalizadosRequest.getLimiteCliente().getCodMoneda()+"], [tipoTransaccion="+limitesPersonalizadosRequest.getLimiteCliente().getTipoTransaccion()+"]", request.getRemoteAddr());
			}else {
				if(accion.equals(GUARDARLIMITECLIENTE)) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" LimitesPersonalizados:[codigoIbs="+limitesPersonalizadosRequest.getLimiteCliente().getCodigoIbs()+"], "
							+ "[codMoneda="+limitesPersonalizadosRequest.getLimiteCliente().getCodMoneda()+"], [tipoTransaccion="+limitesPersonalizadosRequest.getLimiteCliente().getTipoTransaccion()+"], [montoMin="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMin()+"], "
							+ "[montoMax="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMax()+"], [montoTope="+limitesPersonalizadosRequest.getLimiteCliente().getMontoTope()+"], [montoMensual="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMensual()+"], "
							+ "[montoDiario="+limitesPersonalizadosRequest.getLimiteCliente()+"]", request.getRemoteAddr());
				}else {
					if(accion.equals(SAVELIMITECLIENTE)) {
						auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
								CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" LimitesPersonalizados:[codigoIbs="+limitesPersonalizadosRequest.getLimiteCliente().getCodigoIbs()+"], "
								+ "[codMoneda="+limitesPersonalizadosRequest.getLimiteCliente().getCodMoneda()+"], [tipoTransaccion="+limitesPersonalizadosRequest.getLimiteCliente().getTipoTransaccion()+"], [montoMin="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMin()+"], "
								+ "[montoMax="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMax()+"], [montoTope="+limitesPersonalizadosRequest.getLimiteCliente().getMontoTope()+"], [montoMensual="+limitesPersonalizadosRequest.getLimiteCliente().getMontoMensual()+"], "
								+ "[montoDiario="+limitesPersonalizadosRequest.getLimiteCliente().getMontoDiario()+"]", request.getRemoteAddr());
					}
				}
			}
						
			LOGGER.info(LIMITESPERSONALIZADOSFUNCIONAUDITORIAI);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public List<LimitesPersonalizados> listaLimitesPersonalizados(
			LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICELISTAF);
				return respuesta2xxListaLimitesPersonalizados(retorno, accion, limitesPersonalizadosRequest, request);
			}else {
				//LOGGER.error(respuesta4xxListaLimitesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaLimitesPersonalizados(retorno, accion, limitesPersonalizadosRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesPersonalizados> respuesta2xxListaLimitesPersonalizados(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			guardarAuditoria(accion, true, limiteResponse.getResultado().getCodigo(), limiteResponse.getResultado().getDescripcion(), request);
			return limiteResponse.getLimitesPersonalizados();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return new ArrayList<>();
		}
        
	}

	public String respuesta4xxListaLimitesPersonalizados(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoria(accion, false, resultado.getCodigo(), resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return null;
		}
	}


	@Override
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno, accion, limitesPersonalizadosRequest, request);
			}else {
				//LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno, accion, limitesPersonalizadosRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	
	}
	
	public String respuesta2xxActualizarCrear(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			if(accion.equals("activarLimiteCliente") || accion.equals("desactivarLimiteCliente")) {
				guardarAuditoriaActivarDesactivarLimites(accion, true, response.getResultado().getCodigo(), 
						limitesPersonalizadosRequest, response.getResultado().getDescripcion(),request);
			}else {
				if(accion.equals(GUARDARLIMITECLIENTE)){
					guardarAuditoriaEditLimites(accion, true, response.getResultado().getCodigo(),
							limitesPersonalizadosRequest, response.getResultado().getDescripcion(), request);
				}
			}
			
			
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals("activarLimiteCliente") || accion.equals("desactivarLimiteCliente")) {
				guardarAuditoriaActivarDesactivarLimites(accion, false, String.valueOf(retorno.getStatus()), 
					limitesPersonalizadosRequest, retorno.getBody(),request);
			}else {
				if(accion.equals(GUARDARLIMITECLIENTE)){
					guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
							limitesPersonalizadosRequest, retorno.getBody(), request);
				}
			}	
			return null;
		}
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			if(accion.equals("activarLimiteCliente") || accion.equals("desactivarLimiteCliente")) {
				guardarAuditoriaActivarDesactivarLimites(accion, false, response.getResultado().getCodigo(), 
					limitesPersonalizadosRequest, response.getResultado().getDescripcion(),request);
			}else {
				if(accion.equals(GUARDARLIMITECLIENTE)){
					guardarAuditoriaEditLimites(accion, false, response.getResultado().getCodigo(),
							limitesPersonalizadosRequest, response.getResultado().getDescripcion(), request);
				}
			}	
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals("activarLimiteCliente") || accion.equals("desactivarLimiteCliente")) {
				guardarAuditoriaActivarDesactivarLimites(accion, false, String.valueOf(retorno.getStatus()), 
					limitesPersonalizadosRequest, retorno.getBody(),request);
			}else {
				if(accion.equals(GUARDARLIMITECLIENTE)){
					guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
							limitesPersonalizadosRequest, retorno.getBody(), request);
				}
			}		
			return null;
		}
	}


	@Override
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest,
			String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICEBUSCARF);
				return respuesta2xxBuscarLimitesPersonalizados(retorno, accion, limitesPersonalizadosRequest, request);
			}else {
				//LOGGER.error(respuesta4xxListaLimitesPersonalizados(retorno));
				throw new CustomException(respuesta4xxBuscarLimitesPersonalizados(retorno, accion,
						limitesPersonalizadosRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public LimitesPersonalizados respuesta2xxBuscarLimitesPersonalizados(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest, HttpServletRequest request){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			guardarAuditoriaEditLimites(accion, true, limiteResponse.getResultado().getCodigo(),
					limitesPersonalizadosRequest, limiteResponse.getResultado().getDescripcion(), request);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimitesPersonalizados().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
					limitesPersonalizadosRequest, retorno.getBody(), request);
			return null;
		}
        
	}
	
	public String respuesta4xxBuscarLimitesPersonalizados(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest, HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaEditLimites(accion, false, resultado.getCodigo(),
					limitesPersonalizadosRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
					limitesPersonalizadosRequest, retorno.getBody(), request);
			return null;
		}
	}


	@Override
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESPERSONALIZADOSSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESPERSONALIZADOSSERVICECREARF);
				return respuesta2xxCrear(retorno, accion, limitesPersonalizadosRequest, request);
			}else {
				LOGGER.error(respuesta4xxCrear(retorno, accion, limitesPersonalizadosRequest, request));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public String respuesta2xxCrear(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaEditLimites(accion, true, response.getResultado().getCodigo(),
						limitesPersonalizadosRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
					guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
							limitesPersonalizadosRequest, retorno.getBody(), request);
				
			return null;
		}
		
	}
	
	public String respuesta4xxCrear(WSResponse retorno, String accion, 
			LimitesPersonalizadosRequest limitesPersonalizadosRequest,  HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
					guardarAuditoriaEditLimites(accion, false, response.getResultado().getCodigo(),
							limitesPersonalizadosRequest, response.getResultado().getDescripcion(), request);	
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
					guardarAuditoriaEditLimites(accion, false, String.valueOf(retorno.getStatus()),
							limitesPersonalizadosRequest, retorno.getBody(), request);		
			return null;
		}
	}

}
