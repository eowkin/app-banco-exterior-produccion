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

import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteDatosBasicosResponse;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;


@Service
public class ClientePersonalizadoServiceApiRestImpl implements IClientePersonalizadoServiceApiRest{
	
	private static final Logger LOGGER = LogManager.getLogger(ClientePersonalizadoServiceApiRestImpl.class);

	
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
    
    @Value("${${app.ambiente}"+".clientesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".clientesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    @Value("${${app.ambiente}"+".datosbasicos.urlConsultaDatosBasicos}")
    private String urlConsultaDatosBasicos;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio clientesPersonalizados";
    
    private static final String CLIENTESPERSONALIZADOSSERVICELISTAI = "[==== INICIO Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAF = "[==== FIN Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEBUSCARI = "[==== INICIO Buscar ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEBUSCARF = "[==== FIN Buscar ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEACTUALIZARI = "[==== INICIO Actualizar ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEACTUALIZARF = "[==== FIN Actualizar ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICECREARI = "[==== INICIO Crear ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICECREARF = "[==== FIN Crear ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSI = "[==== INICIO BuscarDatosBasicos ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSF = "[==== FIN BuscarDatosBasicos ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONI = "[==== INICIO Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONF = "[==== FIN Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  ClientesPersonalizados - Funcion-Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  ClientesPersonalizados - Funcion-Service ====]";
	
	private static final String CLIENTESPERSONALIZADOS = "clientesPersonalizados";
	
	private static final String INDEX = "index";
	
	private static final String ACTIVAR = "Activar";
	
	private static final String DESACTIVAR = "Desactivar";
	
	private static final String ERROR = "Error: ";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operacion Exitosa.";
	
	private static final String SEARCHCREAR = "searchCrear";
	
	private static final String SEARCH = "search";
	
	private static final String SEARCHNROIDCLIENTE = "searchNroIdCliente";
	
	
	
	private static final String FORMLIMITECLIENTEPERSONALIZADO = "formLimiteClientePersonalizado";
	
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	@Override
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest)
			throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAF);
				return respuesta2xxListaClientesPersonalizados(retorno);
			} else {
				LOGGER.error(respuesta4xxListaClientesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public List<ClientesPersonalizados> respuesta2xxListaClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			return clienteResponse.getListaClientes();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
		
	}
	
	public String respuesta4xxListaClientesPersonalizados(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEBUSCARF);
				return respuesta2xxbuscarClientesPersonalizados(retorno);
			} else {
				LOGGER.error(respuesta4xxListaClientesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			LOGGER.info(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClientesPersonalizados respuesta2xxbuscarClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse.getListaClientes().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
		
	}
	
	
	@Override
	public String actualizar(ClienteRequest clienteRequest) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);	
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.info(ERRORMICROCONEXION);
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
	public String crear(ClienteRequest clienteRequest) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICECREARF);
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

	public WSResponse consultarWs(ClienteRequest clienteRequest) {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		LOGGER.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest)
			throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteDatosBasicoRequestJSON;
		clienteDatosBasicoRequestJSON = new Gson().toJson(clienteDatosBasicoRequest);
		wsrequest.setBody(clienteDatosBasicoRequestJSON);
		wsrequest.setUrl(urlConsultaDatosBasicos);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSF);
				return respuesta2xxBuscarDatosBasicos(retorno);	
			} else {
				LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	
	public DatosClientes respuesta2xxBuscarDatosBasicos(WSResponse retorno) {
		try {
			ClienteDatosBasicosResponse clienteDatosBasicosResponse  = mapper.jsonToClass(retorno.getBody(), ClienteDatosBasicosResponse.class);
			if(clienteDatosBasicosResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteDatosBasicosResponse.getDatosCliente();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new DatosClientes();
		}
		
	}
	
	
	@Override
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONF);
				return respuesta2xxListaClientesPaginacion(retorno);
			} else {
				LOGGER.error(respuesta4xxListaClientesPaginacion(retorno));
				throw new CustomException(respuesta4xxListaClientesPaginacion(retorno));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClienteResponse respuesta2xxListaClientesPaginacion(WSResponse retorno) {
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxListaClientesPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	public void guardarAuditoriaActivarDesactivar(String accion, boolean resultado, String codRespuesta, String codigoIbs, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs="+codigoIbs+"]", request.getRemoteAddr());
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaCodigo(String accion, boolean resultado, String codRespuesta, String codigoIbs, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs="+codigoIbs+"]", request.getRemoteAddr());
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	public void guardarAuditoriaClientePersonalizado(String accion, boolean resultado, String codRespuesta, ClienteRequest clienteRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs="+clienteRequest.getCliente().getCodigoIbs()+"], [nroIdCliente="+clienteRequest.getCliente().getNroIdCliente()+"], [nombreRif="+clienteRequest.getCliente().getNombreRif()+"]", request.getRemoteAddr());
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaClientePersonalizadoSEARCHCREAR(String accion, boolean resultado, String codRespuesta, ClienteDatosBasicoRequest clienteDatosBasicoRequest, String respuesta, HttpServletRequest request) {
		try {
			String codigoIbs = "";
			if(clienteDatosBasicoRequest.getCodigoIbs() != null) {
				codigoIbs = clienteDatosBasicoRequest.getCodigoIbs();
			}
			
			String nroIdCliente = "";
			if(clienteDatosBasicoRequest.getNroIdCliente() != null) {
				nroIdCliente = clienteDatosBasicoRequest.getNroIdCliente();
			}
			
			
			
			
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs="+codigoIbs+"], [nroIdCliente="+nroIdCliente+"]", request.getRemoteAddr());
			
			
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	
	
	
	public void guardarAuditoriaSearchClientePersonalizado(String accion, boolean resultado, String codRespuesta, ClienteRequest clienteRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAI);
			if(accion.equals(SEARCH)) {
				if(clienteRequest.getCliente().getCodigoIbs() != null) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs="+clienteRequest.getCliente().getCodigoIbs()+"]", request.getRemoteAddr());
				}else {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados:[codigoIbs=]", request.getRemoteAddr());
				}
				
			}else {
				if(accion.equals(SEARCHNROIDCLIENTE)) {
					if(clienteRequest.getCliente().getNroIdCliente() != null) {
						auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
								CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados: [nroIdCliente="+clienteRequest.getCliente().getNroIdCliente()+"]", request.getRemoteAddr());
					}else {
						auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
								CLIENTESPERSONALIZADOS, accion, codRespuesta, resultado, respuesta+" ClientesPersonalizados: [nroIdCliente=]", request.getRemoteAddr());
					}
					
				}	
			}
			
			LOGGER.info(CLIENTESPERSONALIZADOSFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONF);
				return respuesta2xxListaClientesPaginacion(retorno, accion, clienteRequest, request);
			} else {
				//LOGGER.error(respuesta4xxListaClientesPaginacion(retorno));
				throw new CustomException(respuesta4xxListaClientesPaginacion(retorno, accion, clienteRequest, request));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public ClienteResponse respuesta2xxListaClientesPaginacion(WSResponse retorno, String accion, ClienteRequest clienteRequest, 
			HttpServletRequest request) {
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, true, clienteResponse.getResultado().getCodigo(),  clienteResponse.getResultado().getDescripcion(), request);
			}else {
				if(accion.equals(SEARCH) || accion.equals(SEARCHNROIDCLIENTE)) {
					guardarAuditoriaSearchClientePersonalizado(accion, true, clienteResponse.getResultado().getCodigo(), clienteRequest, clienteResponse.getResultado().getDescripcion(), request);
				}
			}
			
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), request);
			}else {
				if(accion.equals(SEARCH) || accion.equals(SEARCHNROIDCLIENTE)) {
					guardarAuditoriaSearchClientePersonalizado(accion, false, String.valueOf(retorno.getStatus()), clienteRequest, retorno.getBody(), request);
				}
			}
			return null;
		}
		
	}
	
	public String respuesta4xxListaClientesPaginacion(WSResponse retorno, String accion, ClienteRequest clienteRequest, 
			HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, resultado.getCodigo(),  resultado.getDescripcion(), request);
			}else {
				if(accion.equals(SEARCH) || accion.equals(SEARCHNROIDCLIENTE)) {
					guardarAuditoriaSearchClientePersonalizado(accion, false, resultado.getCodigo(), clienteRequest, resultado.getDescripcion(), request);
				}
			}	
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), request);
			}else {
				if(accion.equals(SEARCH) || accion.equals(SEARCHNROIDCLIENTE)) {
					guardarAuditoriaSearchClientePersonalizado(accion, false, String.valueOf(retorno.getStatus()), clienteRequest, retorno.getBody(), request);
				}
			}	
			return null;
		}
	}

	@Override
	public String actualizar(ClienteRequest clienteRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);	
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno, accion, clienteRequest, request);
			}else {
				//LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno, accion, clienteRequest, request));
			}
		}else {
			LOGGER.info(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public String respuesta2xxActualizarCrear(WSResponse retorno, String accion, ClienteRequest clienteRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaActivarDesactivar(accion, true, response.getResultado().getCodigo(), clienteRequest.getCliente().getCodigoIbs(), response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, ClienteRequest clienteRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaActivarDesactivar(accion, false, response.getResultado().getCodigo(), clienteRequest.getCliente().getCodigoIbs(), response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaActivarDesactivar(accion, false, String.valueOf(retorno.getStatus()), clienteRequest.getCliente().getCodigoIbs(),  retorno.getBody(), request);
			return null;
		}
	}

	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEBUSCARF);
				return respuesta2xxbuscarClientesPersonalizados(retorno, accion, clienteRequest, request);
			} else {
				//LOGGER.error(respuesta4xxListaClientesPersonalizados(retorno));
				throw new CustomException(respuesta4xxbuscarClientesPersonalizados(retorno, accion, clienteRequest, request));
			}
		} else {
			LOGGER.info(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public ClientesPersonalizados respuesta2xxbuscarClientesPersonalizados(WSResponse retorno, String accion, ClienteRequest clienteRequest,
			HttpServletRequest request){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			guardarAuditoriaCodigo(accion, true, clienteResponse.getResultado().getCodigo(), clienteRequest.getCliente().getCodigoIbs(), clienteResponse.getResultado().getDescripcion(), request);
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse.getListaClientes().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), clienteRequest.getCliente().getCodigoIbs(), retorno.getBody(), request);
			return null;
		}
		
		
	}
	
	public String respuesta4xxbuscarClientesPersonalizados(WSResponse retorno, String accion, ClienteRequest clienteRequest,
			HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaCodigo(accion, false, resultado.getCodigo(), clienteRequest.getCliente().getCodigoIbs(), resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), clienteRequest.getCliente().getCodigoIbs(), retorno.getBody(), request);
			return null;
		}
	}

	@Override
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteDatosBasicoRequestJSON;
		clienteDatosBasicoRequestJSON = new Gson().toJson(clienteDatosBasicoRequest);
		wsrequest.setBody(clienteDatosBasicoRequestJSON);
		wsrequest.setUrl(urlConsultaDatosBasicos);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSF);
				return respuesta2xxBuscarDatosBasicos(retorno, accion, clienteDatosBasicoRequest, request);	
			} else {
				//LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxBuscarDatosBasicos(retorno, accion, clienteDatosBasicoRequest,request));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public DatosClientes respuesta2xxBuscarDatosBasicos(WSResponse retorno, String accion, ClienteDatosBasicoRequest clienteDatosBasicoRequest, 
			HttpServletRequest request) {
		try {
			ClienteDatosBasicosResponse clienteDatosBasicosResponse  = mapper.jsonToClass(retorno.getBody(), ClienteDatosBasicosResponse.class);
			guardarAuditoriaClientePersonalizadoSEARCHCREAR(accion, true, clienteDatosBasicosResponse.getResultado().getCodigo(),clienteDatosBasicoRequest, clienteDatosBasicosResponse.getResultado().getDescripcion(), request);
			if(clienteDatosBasicosResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteDatosBasicosResponse.getDatosCliente();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaClientePersonalizadoSEARCHCREAR(accion, false, String.valueOf(retorno.getStatus()),clienteDatosBasicoRequest, retorno.getBody(), request);
			return new DatosClientes();
		}
		
	}
	
	public String respuesta4xxBuscarDatosBasicos(WSResponse retorno, String accion, ClienteDatosBasicoRequest clienteDatosBasicoRequest, 
			HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaClientePersonalizadoSEARCHCREAR(accion, false, response.getResultado().getCodigo(),clienteDatosBasicoRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaClientePersonalizadoSEARCHCREAR(accion, false, String.valueOf(retorno.getStatus()),clienteDatosBasicoRequest, retorno.getBody(), request);
			return null;
		}
	}

	@Override
	public String crear(ClienteRequest clienteRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(CLIENTESPERSONALIZADOSSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(CLIENTESPERSONALIZADOSSERVICECREARF);
				return respuesta2xxCrear(retorno, accion, clienteRequest, request);
			}else {
				//LOGGER.error(respuesta4xxCrear(retorno));
				throw new CustomException(respuesta4xxCrear(retorno, accion, clienteRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public String respuesta2xxCrear(WSResponse retorno, String accion, ClienteRequest clienteRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaClientePersonalizado(accion, true, response.getResultado().getCodigo(), clienteRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaClientePersonalizado(accion, false, String.valueOf(retorno.getStatus()), clienteRequest, retorno.getBody(), request);
			return null;
		}
		
	}
	
	public String respuesta4xxCrear(WSResponse retorno, String accion, ClienteRequest clienteRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaClientePersonalizado(accion, false, response.getResultado().getCodigo(), clienteRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaClientePersonalizado(accion, false, String.valueOf(retorno.getStatus()), clienteRequest, retorno.getBody(), request);
			return null;
		}
	}

}
