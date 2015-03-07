package com.boot.pf.helper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.boot.pf.controller.BrowserStatsView;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.primefaces.push.impl.JSONEncoder;

/**
 * Filter to keep track of browsers of visitors, mapped to the demo page /push/chart.jsf
 */
/**
 * Created by Maximos on 3/5/2015.
 */
public class UserAgentListener implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("UserAgentFilter started");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String agent = ((HttpServletRequest) req).getHeader("User-Agent");
        Map<String,Integer> agents = BrowserStatsView.agents;
        if(agent != null) {
            boolean match = false;


            for (Map.Entry<String, Integer> entry : agents.entrySet()) {
                String key = entry.getKey();
                if(agent.indexOf(key) != -1) {
                    Integer value = agents.get(key);
                    agents.put(key, ++value);
                    match = true;
                    break;
                }
            }

            if(match == false) {
                Integer value = agents.get("Other");
                agents.put("Other", ++value);
            }

            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish("/browser",  new JSONEncoder().encode(agents));
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }

}