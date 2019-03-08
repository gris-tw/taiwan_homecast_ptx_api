/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.citybike;

import com.gris.tw.ptx.PTXPlatform;
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
public class BikeStopAssistant {
    
    private String stationUID = "";
    private JSONObject bikeData;
    
    public BikeStopAssistant(BikeInfo.City city, String stationUID) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        this.stationUID = stationUID;
        
        PTXPlatform ptxp =new PTXPlatform("https://ptx.transportdata.tw/MOTC/v2/Bike/Availability/Kaohsiung?$filter=" + URLEncoder.encode("StationUID eq '" + stationUID + "'" , "UTF-8") + "&$top=30&$format=JSON");
        bikeData = new JSONArray(ptxp.getData()).getJSONObject(0);
    }
    
    public int getAvaliableBike(){
        return bikeData.getInt("AvailableRentBikes");
    }
}
