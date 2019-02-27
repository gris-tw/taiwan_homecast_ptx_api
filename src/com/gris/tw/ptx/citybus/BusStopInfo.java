/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.citybus;

import com.gris.tw.Homecast;
import com.gris.tw.ptx.PTXPlatform;
import java.net.URLEncoder;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class BusStopInfo implements Homecast{
    
    protected BusRouteInfo route;
    protected JSONArray routeData;
    protected String usageLanguage;
    
    protected String StopUID = "";
    protected String RouteUID = "";
    protected int Direction;
    
    public BusStopInfo(BusRouteInfo route) throws Exception{
        this.route = route;
        this.usageLanguage = route.getUsageLanguage();
        this.RouteUID = route.getRouteUID();
        
        
        String routeUIDEncoding = URLEncoder.encode(RouteUID, "UTF-8");
        PTXPlatform ptxp = new PTXPlatform("https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/" + route.getCityName() + "?$filter=" + URLEncoder.encode("contains(RouteUID, '" + routeUIDEncoding + "') ", "UTF-8") + "&$format=JSON");
        this.routeData = new JSONArray(ptxp.getData());
    }
    
    protected BusRouteInfo getBusRouteInfo(){
        return route;
    }
    
    
    public HashMap<String, String> CountryCodeSupport(){
        HashMap<String, String> lang = new HashMap<>();
        
        lang.put("Zh_tw", "繁體中文");
        lang.put("en", "English");
        
        return lang;
    }
    
    public JSONArray getStopInformation(){
        JSONArray stops = new JSONArray();
        
        for (int i = 0; i < routeData.length(); i++) {
            JSONObject specRoute = routeData.getJSONObject(i);
            
            JSONObject filterStopName = new JSONObject();
            filterStopName.put("StopName", specRoute.getJSONObject("StopName").getString(this.usageLanguage));
            filterStopName.put("StopUID", specRoute.getString("StopUID"));
            filterStopName.put("StopID", specRoute.getString("StopID"));
            filterStopName.put("RouteUID", specRoute.getString("RouteUID"));
            filterStopName.put("RouteID", specRoute.getString("RouteID"));
            filterStopName.put("RouteName", specRoute.getJSONObject("RouteName").getString(this.usageLanguage));
            filterStopName.put("Direction", specRoute.getInt("Direction"));
            stops.put(filterStopName);
        }
        
        return stops;
    }
    
    public void setStop(String StopUID, int Direction){
        this.StopUID = StopUID;
        this.Direction = Direction;
    }
    
}
