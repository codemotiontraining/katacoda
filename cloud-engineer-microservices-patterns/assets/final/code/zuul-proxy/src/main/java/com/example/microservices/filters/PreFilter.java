package com.example.microservices.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.microservices.domain.Credentials;
import com.example.microservices.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class PreFilter extends ZuulFilter {

  private final static String AUTHORIZATION_HEADER = "Authorization";
  private final static String LOGIN_PATH = "login";

  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(PreFilter.class.getName());
  private final static String PRE_FILTER_UNAUTHORIZED_LOG = "Time: [%s] Request [Method: %s, URL: %s], User is not authorized.";
  private final static String PRE_FILTER_AUTHORIZED_LOG = "Time: [%s] Request [Method: %s, URL: %s], User is authorized.";
  private final static String PRE_FILTER_EXCEPTION_LOG = "Time: [%s] Exception: %s";

  private final static String newLine = System.getProperty("line.separator");

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AuthService authService;

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    if(!request.getRequestURI().contains(LOGIN_PATH)) {
       if(request.getHeader(AUTHORIZATION_HEADER)==null
               || !authService.validateToken(request.getHeader(AUTHORIZATION_HEADER))) {
         unauthorizedResponse(ctx, request);
         return null;
       }
      log.info(String.format(PRE_FILTER_AUTHORIZED_LOG, LocalDateTime.now().toString(),
                                                        request.getMethod(),
                                                        request.getRequestURL().toString()));
       return null;
    }

    String token = null;
    try {
      Credentials credentials = objectMapper.readValue(ctx.getRequest().getInputStream(), Credentials.class);
      token = authService.getToken(credentials.getUsername(), credentials.getPassword());
    } catch (Exception e) {
      log.severe(String.format(PRE_FILTER_EXCEPTION_LOG, LocalDateTime.now().toString(), e.toString()));
    } finally {
      if (token==null) {
        unauthorizedResponse(ctx, request);
        return null;
      }
      loginSuccessResponse(ctx, request, token);
    }

    return null;
  }

  private void loginSuccessResponse(RequestContext ctx, HttpServletRequest request, String token) {
    log.info(String.format(PRE_FILTER_AUTHORIZED_LOG, LocalDateTime.now().toString(),
                                                      request.getMethod(),
                                                      request.getRequestURL().toString()));
    ctx.setSendZuulResponse(false);
    ctx.setResponseStatusCode(200);
    HttpServletResponse response = ctx.getResponse();
    response.setHeader(AUTHORIZATION_HEADER, token);
    ctx.setResponse(response);
  }


  private void unauthorizedResponse(RequestContext ctx, HttpServletRequest request) {
    log.severe(String.format(PRE_FILTER_UNAUTHORIZED_LOG, LocalDateTime.now().toString(),
                                                          request.getMethod(),
                                                          request.getRequestURL().toString()));
    ctx.setSendZuulResponse(false);
    ctx.setResponseStatusCode(401);
  }


}