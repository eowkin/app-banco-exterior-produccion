package com.bancoexterior.app.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.bancoexterior.app.inicio.service.IAuditoriaService;


@Component
@Controller
public class CustomLogoutHandler extends SecurityContextLogoutHandler{

	
	private IAuditoriaService auditoriaService;
	
	public CustomLogoutHandler(IAuditoriaService auditoriaService) {
	    this.auditoriaService = auditoriaService;
	  }
	
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
	    
	    
	    auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(), "Logout", "Fin Sesion", "N/A", true, "Finalizar de Sesion", request.getRemoteAddr());
	  }
}
