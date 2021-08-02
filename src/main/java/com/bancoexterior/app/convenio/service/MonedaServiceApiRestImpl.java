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

import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;





@Service
public class MonedaServiceApiRestImpl implements IMonedaServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(MonedaServiceApiRestImpl.class);	
    
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
    
    @Value("${${app.ambiente}"+".moneda.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".moneda.urlActualizar}")
    private String urlActualizar;

    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Monedas";
    
    private static final String MONEDASERVICELISTAMONEDASI = "[==== INICIO Lista Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICELISTAMONEDASF = "[==== FIN Lista Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEEXISTEMONEDAI = "[==== INICIO Existe Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEEXISTEMONEDAF = "[==== FIN Existe Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEBUSCARMONEDAI = "[==== INICIO Buscar Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEBUSCARMONEDAF = "[==== FIN Buscar Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEACTUALIZARMONEDAI = "[==== INICIO Actualizar Monedas - Service ====]";
	
	private static final String MONEDASERVICEACTUALIZARMONEDAF = "[==== FIN Actualizar Monedas - Service ====]";
	
	private static final String MONEDASERVICECREARMONEDAI = "[==== INICIO Crear Monedas - Service ====]";
	
	private static final String MONEDASERVICECREARMONEDAF = "[==== FIN Crear Monedas - Service ====]";
	
	
	//probando auditoria
	
	private static final String MONEDAFUNCIONAUDITORIAACTIVARDESACTIVARI = "[==== INICIO Guardar Auditoria Activar/Desactivar Moneda - Controller ====]";
	
	private static final String MONEDAFUNCIONAUDITORIAACTIVARDESACTIVARF = "[==== FIN Guardar Auditoria Activar/Desactivar Moneda - Controller ====]";
	
	private static final String MONEDAFUNCIONAUDITORIASEARCHCODIGOI = "[==== INICIO Guardar Auditoria SearchCodigo Moneda - Controller ====]";
	
	private static final String MONEDAFUNCIONAUDITORIASEARCHCODIGOF = "[==== FIN Guardar Auditoria SearchCodigo Moneda - Controller ====]";
	
	private static final String MONEDAFUNCIONAUDITORIAINDEXI = "[==== INICIO Guardar Auditoria Index Moneda - Controller ====]";
	
	private static final String MONEDAFUNCIONAUDITORIAINDEXF = "[==== FIN Guardar Auditoria Index Moneda - Controller ====]";
	
	private static final String INDEX = "index";
	
	private static final String MONEDAS = "Monedas";
	
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }



	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICELISTAMONEDASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICELISTAMONEDASF);
	            return respuesta2xxlistaMonedas(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(MONEDASERVICELISTAMONEDASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		LOGGER.info("retorno: "+retorno);
		
		if(retorno.isExitoso()) {
			LOGGER.info("retorno.getStatus(): "+retorno.getStatus());
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICELISTAMONEDASF);
	            return respuesta2xxlistaMonedas(retorno, accion, monedasRequest, request);
			}else {
				//LOGGER.error(respuesta4xx(retorno, accion, monedasRequest, request));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Moneda> respuesta2xxlistaMonedas(WSResponse retorno, String accion, MonedasRequest monedasRequest, HttpServletRequest request){
		try {
			
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			if(accion.equals(INDEX)) {
				guardarAuditoriaIndex(accion, true, monedaResponse.getResultado().getCodigo(), monedaResponse.getResultado().getDescripcion(),request);
			}else {
			  	                         		
				guardarAuditoriaSearchCodigo(accion, true, monedaResponse.getResultado().getCodigo(), monedasRequest.getMoneda().getCodMoneda(), monedaResponse.getResultado().getDescripcion(),request);
			}
			
			return monedaResponse.getMonedas();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX)) {
				guardarAuditoriaIndex(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(),request);
			}else {
			  	                         		
				guardarAuditoriaSearchCodigo(accion, false, String.valueOf(retorno.getStatus()), monedasRequest.getMoneda().getCodMoneda(), retorno.getBody(),request);
			}
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xx(WSResponse retorno, String accion, MonedasRequest monedasRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals(INDEX)) {
				guardarAuditoriaIndex(accion, false, resultado.getCodigo(), resultado.getDescripcion(),request);
			}else {
           		
				guardarAuditoriaSearchCodigo(accion, true, resultado.getCodigo(), monedasRequest.getMoneda().getCodMoneda(), resultado.getDescripcion(),request);
			}	
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX)) {
				guardarAuditoriaIndex(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(),request);
			}else {
           		
				guardarAuditoriaSearchCodigo(accion, true, String.valueOf(retorno.getStatus()), monedasRequest.getMoneda().getCodMoneda(), retorno.getBody(),request);
			}
			return null;
		}
	}
	
	
	
	
	
	
	
	
	public List<Moneda> respuesta2xxlistaMonedas(WSResponse retorno){
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getMonedas();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}



	@Override
	public boolean existe(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICEEXISTEMONEDAI);  
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEEXISTEMONEDAF);
				return respuesta2xxExiste(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}

	public boolean respuesta2xxExiste(WSResponse retorno) {
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getResultado().getCodigo().equals("0000") ? true:false;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return false;
		}

	}
	
	
	
	public String respuesta4xx(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICEBUSCARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEBUSCARMONEDAF);
				return respuest2xxBuscarMoneda(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
		
	}

	public Moneda respuest2xxBuscarMoneda(WSResponse retorno) {
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			if(monedaResponse.getResultado().getCodigo().equals("0000")){
	        	return monedaResponse.getMonedas().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}

	@Override
	public String actualizar(MonedasRequest monedasRequest) throws CustomException{
		LOGGER.info(MONEDASERVICEACTUALIZARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEACTUALIZARMONEDAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}

	
	@Override
	public String actualizar(MonedasRequest monedasRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(MONEDASERVICEACTUALIZARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEACTUALIZARMONEDAF);
				return respuesta2xxActualizarCrear(retorno, accion, monedasRequest, request);
				
			}else {
				
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno, accion, monedasRequest, request));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	
	public String respuesta2xxActualizarCrear(WSResponse retorno, String accion, MonedasRequest monedasRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaActivarDesactivar(accion, true, response.getResultado().getCodigo(), monedasRequest.getMoneda().getCodMoneda(), response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaActivarDesactivar(accion, false, String.valueOf(retorno.getStatus()), monedasRequest.getMoneda().getCodMoneda(), retorno.getBody(), request);
			return null;
		}
       
	}
	
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, MonedasRequest monedasRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaActivarDesactivar(accion, false, response.getResultado().getCodigo(), monedasRequest.getMoneda().getCodMoneda(), response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaActivarDesactivar(accion, false, String.valueOf(retorno.getStatus()), monedasRequest.getMoneda().getCodMoneda(), retorno.getBody(), request);
			return null;
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



	



	@Override
	public String crear(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICECREARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICECREARMONEDAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				LOGGER.error(respuesta4xxCrear(retorno));
				throw new CustomException(respuesta4xxCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta4xxCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	
	public void guardarAuditoriaIndex(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		LOGGER.info(MONEDAFUNCIONAUDITORIAINDEXI);
		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
				MONEDAS, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
		LOGGER.info(MONEDAFUNCIONAUDITORIAINDEXF);
	}
	
	
	public void guardarAuditoriaActivarDesactivar(String accion, boolean resultado, String codRespuesta, String codMoneda, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MONEDAFUNCIONAUDITORIAACTIVARDESACTIVARI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONEDAS, accion, codRespuesta, resultado, respuesta+" Moneda:[codMoneda="+codMoneda+"]", request.getRemoteAddr());
			LOGGER.info(MONEDAFUNCIONAUDITORIAACTIVARDESACTIVARF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaSearchCodigo(String accion, boolean resultado, String codRespuesta, String codMoneda, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MONEDAFUNCIONAUDITORIASEARCHCODIGOI);
			if(codMoneda != null) {
				if(!codMoneda.equals("")) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							MONEDAS, accion, codRespuesta, resultado, respuesta+" Moneda:[codMoneda="+codMoneda.toUpperCase()+"]", request.getRemoteAddr());
				}else {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							MONEDAS, accion, codRespuesta, resultado, respuesta+" Moneda:[codMoneda=]", request.getRemoteAddr());
				}
			}else {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						MONEDAS, accion, codRespuesta, resultado, respuesta+" Moneda:[codMoneda=]", request.getRemoteAddr());
			}
			
			LOGGER.info(MONEDAFUNCIONAUDITORIASEARCHCODIGOF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}



	



	
	
}
