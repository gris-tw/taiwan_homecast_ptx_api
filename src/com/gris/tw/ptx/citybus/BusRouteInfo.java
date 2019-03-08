/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.citybus;

import com.gris.tw.Homecast;
import com.gris.tw.ptx.PTXPlatform;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class BusRouteInfo implements Homecast{
    
    private String RouteUID = "";
    private String cityName = "";
    private String usageLanguage = "";
    
    private PTXPlatform ptxp;
    private JSONArray routeData;
    public BusRouteInfo(String cityName)throws Exception{
        if(!new BusServiceCityInfo().cityIdMapping().containsValue(cityName)){
            throw new Exception("Wrong city name, Please check again.");
        }
        String cityURL = "https://ptx.transportdata.tw/MOTC/v2/Bus/Route/City/" + cityName + "?$top=10&$format=JSON";
        ptxp = new PTXPlatform(cityURL);
        routeData = new JSONArray(ptxp.getData());
        
        this.cityName = cityName;
    }
    
    protected String getCityName(){
        return cityName;
    }
    
    protected String getRouteUID()throws Exception{
        if(RouteUID.isEmpty()) throw new Exception("RouterUID  is Not Set");
        return RouteUID;
    }
    
    public void setRouteUID(String RouteUID){
        this.RouteUID = RouteUID;
    }
    
    protected String getUsageLanguage(){
        return this.usageLanguage;
    }
    
    public HashMap<String, String> CountryCodeSupport(){
        HashMap <String, String> code = new HashMap<String, String>();
        code.put("Zh_tw", "繁體中文");
        code.put("en", "English");
        return code;
    }
    
    public String getLanguageKey(String code){
        HashMap<String,String> l = new HashMap<>();
        l.put("Zh_tw","Zh_tw");
        l.put("en","en");
        
        if(l.containsKey(code)){
            return l.get(code);
        }else{
            return "Zh_tw";
        }
    }
    
    public JSONArray getRouteName(String languageCode){
        this.usageLanguage = getLanguageKey(languageCode);
        return routeFilter(routeData);
    }
    
    public JSONArray searchRoute(String languageCode, String routeIDLike) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        this.usageLanguage = getLanguageKey(languageCode);
        String cityURL = "https://ptx.transportdata.tw/MOTC/v2/Bus/Route/City/" + this.cityName + "/"+ routeIDLike +"?$format=JSON";
        ptxp = new PTXPlatform(cityURL);
        routeData = new JSONArray(ptxp.getData());
        return routeFilter(routeData);
    }
    
    private JSONArray routeFilter(JSONArray rdata){
        JSONArray pureRouteFilter = new JSONArray();
        
        for (int i = 0; i < rdata.length(); i++) {
            JSONObject route = rdata.getJSONObject(i);
            
            
            JSONObject pureRoute = new JSONObject();
            pureRoute.put("RouteUID", route.getString("RouteUID"));
            pureRoute.put("RouteID", route.getString("RouteID"));
            pureRoute.put("RouteName", route.getJSONObject("RouteName").getString(this.usageLanguage));
            pureRoute.put("SystemRouteName", route.getJSONObject("RouteName").getString("Zh_tw")); //because system is using chinese to query.
            ///go and backward problem
            pureRouteFilter.put(pureRoute);
        }
        
        return pureRouteFilter;
    }
    
}
