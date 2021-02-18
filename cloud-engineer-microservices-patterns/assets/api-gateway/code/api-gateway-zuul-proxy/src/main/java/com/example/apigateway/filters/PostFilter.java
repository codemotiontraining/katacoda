package com.example.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class PostFilter extends ZuulFilter {

  private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(PostFilter.class.getName());
  private final static String POST_FILTER_LOG = "Time: [%s] Request [Method: %s, URL: %s], Response [Status: %d]";

  @Override
  public String filterType() {
    return "post";
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
    HttpServletResponse response = ctx.getResponse();

    log.info(String.format(POST_FILTER_LOG, LocalDateTime.now().toString(),
                                            request.getMethod(),
                                            request.getRequestURL().toString(),
                                            response.getStatus()));
    return null;
  }

}