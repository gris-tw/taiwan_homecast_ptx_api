/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.metro;

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
public class MetroLineInfo extends DataHandler{
    
    private int city_code = 0;
    private JSONArray lineData;
    
    public MetroLineInfo(int city_code) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        super();
        this.city_code = city_code;
        super.setPTXPURL("https://ptx.transportdata.tw/MOTC/v2/Rail/Metro/Line/" + getCityCodeTranslation().get(city_code) + "?$top=30&$format=JSON");
        super.fetchPTXPData();
        lineData = new JSONArray(super.Data);
    }
    
       
    public JSONArray getLineData(String languageCode){
        this.usageLanguage = getLanguageKey(languageCode);
        JSONArray filter = new JSONArray();
        
        for (int i = 0; i < lineData.length(); i++) {
            JSONObject l = lineData.getJSONObject(i);
            
            JSONObject fline = new JSONObject();
            fline.put("LineID", l.getString("LineID"));
            fline.put("LineName", l.getJSONObject("LineName").getString(this.usageLanguage));
            fline.put("LineColor", l.getString("LineColor"));
            filter.put(fline);
        }
        
        return filter;
    }

}
