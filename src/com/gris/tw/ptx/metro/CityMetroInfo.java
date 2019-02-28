/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gris.tw.ptx.metro;

import com.gris.tw.Homecast;
import java.util.HashMap;

/**
 *
 * @author Mac
 */
public class CityMetroInfo implements Homecast{
    public CityMetroInfo(){
        
    }
    
    public HashMap<String, String> CountryCodeSupport(){
        HashMap<String, String> code = new HashMap<>();
        code.put("Zh_tw", "繁體中文");
        code.put("en", "English");
        return code;
    }
    
    public HashMap<String, Integer> getSupportCity(String countryCode){
        HashMap<String, Integer> city = new HashMap<>();
        if(countryCode.equals("en")){
            city.put("Kaohsinug Metro", 1);
            //city.put("Taipei Metro", 2);
            //city.put("Taoyuan Airport Express", 3);
        }else{
            city.put("高雄捷運", 1);
            //city.put("台北捷運", 2);
            //city.put("桃園機場快捷", 3);
        }
        return city;
    }

}
