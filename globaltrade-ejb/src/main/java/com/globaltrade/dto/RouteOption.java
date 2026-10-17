package com.globaltrade.dto;

import java.io.Serializable;

public class RouteOption implements Serializable {
    private String name;
    private String path;
    private String transit;
    private String risk;
    private String customsComplexity;
    private String recommendation;

    public RouteOption() {}

    public RouteOption(String name, String path, String transit, String risk,
                       String customsComplexity, String recommendation) {
        this.name = name;
        this.path = path;
        this.transit = transit;
        this.risk = risk;
        this.customsComplexity = customsComplexity;
        this.recommendation = recommendation;
    }

    public String getName(){return name;} public void setName(String v){name=v;}
    public String getPath(){return path;} public void setPath(String v){path=v;}
    public String getTransit(){return transit;} public void setTransit(String v){transit=v;}
    public String getRisk(){return risk;} public void setRisk(String v){risk=v;}
    public String getCustomsComplexity(){return customsComplexity;} public void setCustomsComplexity(String v){customsComplexity=v;}
    public String getRecommendation(){return recommendation;} public void setRecommendation(String v){recommendation=v;}
}
