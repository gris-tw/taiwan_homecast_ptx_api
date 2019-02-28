/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.metro;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class MetroStationInfo extends DataHandler{
    
    public JSONArray stationData;
    protected String stationID;
    protected String mLineID;
    
    public MetroStationInfo(int city_code, String mLineID) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        super();
        this.mLineID = mLineID;
        
        super.setPTXPURL(("https://ptx.transportdata.tw/MOTC/v2/Rail/Metro/StationOfLine/"+ getCityCodeTranslation().get(city_code) +"?$filter=LineID eq " + URLEncoder.encode("'"+ mLineID +"'", "UTF-8") +" and LineID eq "+ URLEncoder.encode("'" + mLineID + "'", "UTF-8") +"&$top=30&$format=JSON").replaceAll(" ", "%20"));
        super.fetchPTXPData();
        stationData = new JSONArray(super.Data).getJSONObject(0).getJSONArray("Stations");
    }
    
    public JSONArray getStationInfo(String usageLanguage){
        this.usageLanguage = usageLanguage;
        JSONArray filter = new JSONArray();
        
        for (int i = 0; i < stationData.length(); i++) {
            JSONObject station = stationData.getJSONObject(i);
            
            JSONObject sdata = new JSONObject();
            sdata.put("StationID", station.getString("StationID"));
            sdata.put("StationName", station.getJSONObject("StationName").getString(this.usageLanguage));
            filter.put(sdata);
        }
        
        return filter;
    }
    
}
