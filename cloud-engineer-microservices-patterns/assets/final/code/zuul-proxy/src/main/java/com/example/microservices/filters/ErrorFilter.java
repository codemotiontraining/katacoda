package com.example.microservices.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class ErrorFilter extends ZuulFilter {

  private final static String ERROR_FILTER_LOG = "Time: [%s] Request [Method: %s, URL: %s] - Inside Error Filter.";
  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ErrorFilter.class.getName());

  @Override
  public String filterType() {
    return "error";
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

    log.severe(String.format(ERROR_FILTER_LOG, LocalDateTime.now().toString(),
                                               request.getMethod(),
                                               request.getRequestURL().toString()));

    return null;
  }

}