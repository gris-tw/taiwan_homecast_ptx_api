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
public class BusArrivalAssistant extends BusStopInfo implements Homecast{

    
    private String targetStopUID;
    private String targetRouteUID;
    private int targetDirection;
    
    private JSONObject stopData;
    
    public BusArrivalAssistant(BusStopInfo stop) throws Exception{
        super(stop.getBusRouteInfo());
        
        if(stop.RouteUID.isEmpty() || stop.StopUID.isEmpty()){
            throw new Exception("RouteUID & StopUID is not set yet, please use setStop() before use this Assistant.");
        }
        
        this.targetStopUID = stop.StopUID;
        this.targetRouteUID = stop.RouteUID;
        this.targetDirection = stop.Direction;
        
        this.update();
    }
    
    public HashMap<String, String> supportCountryCode(){
        return super.CountryCodeSupport();
    }
    
    public JSONObject getStopInfo(){
        return stopFilter(stopData);
    }
    
    public JSONObject update() throws Exception{
        
        //https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/Kaohsiung?$filter=RouteUID eq 'KHH100' and StopUID eq 'KHH140' and Direction eq '0'&$format=JSON
        String urlString;
        if(this.targetDirection == -1){
            urlString = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/" + route.getCityName() + "?$filter=" + URLEncoder.encode("StopUID eq '" + targetStopUID + "' ", "UTF-8") + "&$format=JSON";
        }else{
            urlString = "https://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/" + route.getCityName() + "?$filter=" + URLEncoder.encode("StopUID eq '" + targetStopUID + "' and Direction eq '"+ targetDirection +"' ", "UTF-8") +  "&$format=JSON";
        }
        urlString = urlString.replaceAll("\\+", "%20");
        PTXPlatform ptxp = new PTXPlatform(urlString);
        
        try{
            this.stopData = new JSONArray(ptxp.getData()).getJSONObject(0);
        }catch(Exception e){
            System.out.println("Maybe Direction are not  using in this route, please set direction to -1.");
        }
        return stopFilter(stopData);
    }
    
    public JSONObject stopFilter(JSONObject sdata){
        JSONObject filter = new JSONObject();
        filter.put("StopName", sdata.getJSONObject("StopName").getString(super.usageLanguage));
        filter.put("StopUID", sdata.getString("StopUID"));
        if(sdata.isNull("EstimateTime")){
            filter.put("EstimateTime", -1);
        }else{
            filter.put("EstimateTime", sdata.getInt("EstimateTime"));
        }
        filter.put("EstimateTimeAvaliable", !sdata.isNull("EstimateTime"));
        
        return filter;
    }
    
}
