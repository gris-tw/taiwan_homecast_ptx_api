/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.citybike;

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
 * @author hpcslag
 */
public class BikeInfo implements Homecast{
    
    private String stationData = null;
    
    public BikeInfo(City city) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception{
        PTXPlatform ptx;
        switch (city) {
            case Kaohsiung:
                ptx = new PTXPlatform("https://ptx.transportdata.tw/MOTC/v2/Bike/Station/Kaohsiung?$format=JSON");
                break;
            case Taipei:
                ptx = new PTXPlatform("https://ptx.transportdata.tw/MOTC/v2/Bike/Station/Taipei?$format=JSON");
               break;
            case Taichung:
                ptx = new PTXPlatform("https://ptx.transportdata.tw/MOTC/v2/Bike/Station/Taichung?$format=JSON");
               break;
            default:
                throw new Exception("Input is not Illegal or not support yet.");
        }
        
        stationData = ptx.getData();
    }
    
    public static enum City {
        Kaohsiung,
        Taipei,
        Taichung
    }
    
    public HashMap<String,String> CountryCodeSupport(){
        HashMap<String,String> l = new HashMap<>();
        l.put("TC","繁體中文");
        return l;
    }
    
    private String citycodeTranslate(String city) throws Exception{
        String cityname = "";
        switch (city) {
            case "高雄市":
                cityname = "Kaohsiung";
                break;
            case "台北市":
                cityname = "Taipei";
                break;
            default:
                throw new Exception("Not Illegal city name or not support yet.");
        }
        return cityname;
    }
    
    public static JSONArray supportCity(){
        JSONArray sc = new JSONArray();
        sc.put("高雄市");
        sc.put("台北市");
        return sc;
    }
    
    public JSONArray getStations(){
        JSONArray sdata = new JSONArray(stationData);
        
        JSONArray stations = new JSONArray();
        for (int i = 0; i < sdata.length(); i++) {
            JSONObject station = sdata.getJSONObject(i);
            
            JSONObject s = new JSONObject();
            s.put("StationName", station.getJSONObject("StationName").getString("Zh_tw"));
            s.put("StationUID", station.getString("StationUID"));
            stations.put(s);
        }
        return stations;
    }
}
