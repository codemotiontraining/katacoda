package com.example.microservices.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class RouteFilter extends ZuulFilter {

  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(RouteFilter.class.getName());
  private final static String ROUTE_FILTER_LOG = "Time: [%s] Request [Method: %s, URL: %s] - Inside Route Filter.";

  @Override
  public String filterType() {
    return "route";
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

    log.info(String.format(ROUTE_FILTER_LOG, LocalDateTime.now().toString(),
                                             request.getMethod(),
                                             request.getRequestURL().toString()));
    return null;
  }

}