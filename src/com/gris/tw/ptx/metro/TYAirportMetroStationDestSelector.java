/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.metro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class TYAirportMetroStationDestSelector extends DataHandler{
    
    protected int city_code;
    protected String mLineID;
    protected String stationID;
    protected String destID;
    protected int TrainType = -1;
    
    private JSONArray destData;
    
    public TYAirportMetroStationDestSelector(int city_code, String mLineID, String stationID) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyManagementException, IOException{
        super();
        
        super.setPTXPURL(("https://ptx.transportdata.tw/MOTC/v2/Rail/Metro/StationTimeTable/TYMC" + "?$filter=StationID eq " + URLEncoder.encode("'" + stationID + "'", "UTF-8") + "  and LineID eq "+ URLEncoder.encode("'" + mLineID + "'", "UTF-8") +"&$top=30&$format=JSON").replaceAll(" ", "%20"));
        super.fetchPTXPData();
        
        this.city_code = city_code;
        this.mLineID = mLineID;
        this.stationID = stationID;
        
        destData = new JSONArray(Data);
    }
    
    public JSONArray getStationDest(String usageLanguage){
        this.usageLanguage = usageLanguage;
        JSONArray filter = new JSONArray();
        
        for (int i = 0; i < destData.length(); i++) {
            JSONObject dest = destData.getJSONObject(i);
            
            JSONObject dfilter = new JSONObject();
            dfilter.put("StationID", dest.getString("StationID"));
            dfilter.put("StationName", dest.getJSONObject("StationName").getString(super.getLanguageKey(usageLanguage)));
            dfilter.put("DestinationStaionID", dest.getString("DestinationStaionID"));
            dfilter.put("DestinationStationName", dest.getJSONObject("DestinationStationName").getString(super.getLanguageKey(usageLanguage)));
           
            filter.put(dfilter);
        }
        
        return filter;
    }
    
    public void setDestination(String DestinationStationID){
        this.destID = DestinationStationID;
    }
    
    public void setTrainType(int type){
        if(type == 1){
            this.TrainType = 1;
        }else if(type == 2){
            this.TrainType = 2;
        }else{
            this.TrainType = -1; //any train type.
        }
    }
    
    public HashMap<Integer, String> getTrainType(String languageCode){
        HashMap<Integer, String> trainType = new HashMap<>();
        if(languageCode.toLowerCase().equals("en")){
            trainType.put(1, "Express");
            trainType.put(2, "Commuter");
        }else{
            trainType.put(1, "直達車");
            trainType.put(2, "普通車");
        }
        return trainType;
    }
}
