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

/**
 *
 * @author Mac
 */
public abstract class DataHandler implements Homecast{
    
    protected String usageLanguage = "";
    private String ptxpURL = "";
    protected String Data = "";
    
    public DataHandler(){
    }
    
    protected void fetchPTXPData() throws NoSuchAlgorithmException, KeyManagementException, IOException{
        PTXPlatform ptxp = new PTXPlatform(ptxpURL);
        Data = ptxp.getData();
    }
    
    @Override
    public HashMap<String, String> CountryCodeSupport() {
       HashMap<String, String> code = new HashMap<String, String>();
       code.put("Zh_tw", "繁體中文");
       code.put("en", "English");
       return code;
    }
    
    protected String getLanguageKey(String code){
        HashMap<String,String> l = new HashMap<>();
        l.put("Zh_tw","Zh_tw");
        l.put("En","En");
        
        if(l.containsKey(code)){
            return l.get(code);
        }else{
            return "Zh_tw";
        }
    }
    
    protected HashMap<Integer, String> getCityCodeTranslation(){
        HashMap<Integer, String> service = new HashMap<>();
        service.put(1, "KRTC");
        service.put(2, "TRTC");
        service.put(3, "TYMC");
        return service;
    }
    
    public void setLanguage(String code){
        this.usageLanguage = getLanguageKey(code);
    }
    
    public void setPTXPURL(String url){
        this.ptxpURL = url;
        System.out.println(url);
    }
    
}
