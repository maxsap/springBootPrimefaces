package com.boot.pf.controller;

import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Maximos on 3/5/2015.
 */
@Named
@Scope("request")
public class BrowserStatsView {

    private PieChartModel pieModel;

    @Autowired
    ServletContext cntx;

    public static Map agents;

    @PostConstruct
    public void init() {
        if(agents == null) {
            agents = new LinkedHashMap<String, Integer>();
            agents.put("MSIE", 0);
            agents.put("Firefox", 0);
            agents.put("Chrome", 0);
            agents.put("Safari", 0);
            agents.put("Opera", 0);
            agents.put("Other", 0);
        }
        pieModel = new PieChartModel();
        pieModel.setData(agents);
        pieModel.setTitle("Browser Stats");
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public Map getAgents() {
        return agents;
    }

    public void setAgents(Map<String, Integer> agents) {
        this.agents = agents;
    }
}