/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.citybus;

import com.gris.tw.Homecast;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class BusServiceCityInfo implements Homecast{
    
    public HashMap<String, String> CountryCodeSupport(){
        HashMap <String, String> code = new HashMap<String, String>();
        code.put("Zh_tw", "繁體中文");
        code.put("en", "English");
        return code;
    }
    
    public HashMap<Integer, String> cityIdMapping(){
        HashMap<Integer, String> city = new HashMap<Integer, String>();
        city.put(1, "Kaohsiung");
        city.put(2, "Taichung");
        city.put(3, "Taipei");
        
        return city;
    }
    
    public JSONArray getCities(){
        JSONArray cities = new JSONArray();
        //now only support Taipei, Kaohsiung, Taichung.
        
        cities.put(new JSONObject().put("city_id", 1).put("Zh_TW","高雄市").put("en","Kaohsiung"));
        cities.put(new JSONObject().put("city_id", 2).put("Zh_TW","台中市").put("en","Taichung"));
        cities.put(new JSONObject().put("city_id", 3).put("Zh_TW","台北市").put("en","Taipei"));
        
        return cities;
        
    }
    
    protected String getCityIdKey(int key) throws Exception{
        HashMap<Integer, String> cityIds = cityIdMapping();
        if(!cityIds.containsKey(key)){
            throw new Exception("City ID does not match in support list, Please check your city_id.");
        }
        
        return cityIds.get(key);
    }
    
}
