package com.gris.tw.ptx.train;


import com.gris.tw.Homecast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hpcslag
 */
public class NationalStationInfo implements Homecast{

    private JSONArray Cities;
    private JSONArray Stations;

    public NationalStationInfo() {
        try{
            String city_data = HttpPost("http://twtraffic.tra.gov.tw/twrail/Services/BaseDataServ.ashx", "datatype=city&language=tw");
            Cities = new JSONArray(city_data);
            
            
            String station_data = HttpPost("http://twtraffic.tra.gov.tw/twrail/Services/BaseDataServ.ashx", "datatype=station&language=tw");
            Stations = new JSONArray(station_data);
            
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Can't Fetch TRA Website Cities/Station Data.");
        }
    }

    private String HttpPost(String url, String param) throws MalformedURLException, ProtocolException, IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = param;//"datatype=city&language=tw";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
    
    public HashMap<String,String> CountryCodeSupport(){
        HashMap<String,String> l = new HashMap<>();
        l.put("JP","日本語");
        l.put("KR","한국어한국어");
        l.put("SC","简体中文");
        l.put("TC","繁體中文");
        l.put("EN","English");
        return l;
    }
    
    private String getLanguageKey(String code){
        HashMap<String,String> l = new HashMap<>();
        l.put("JP","Name_JP");
        l.put("KR","Name_KR");
        l.put("SC","Name_SC");
        l.put("TC","Name");
        l.put("EN","EName");
        
        if(l.containsKey(code)){
            return l.get(code);
        }else{
            return "City_Name";
        }
    }

    public JSONArray getCities(String country_code) {
        
        
        JSONArray filter = new JSONArray();
        for (int i = 0; i < Cities.length(); i++) {
            JSONObject city = Cities.getJSONObject(i);
            
            JSONObject c = new JSONObject();
            c.put("City_Name", city.getString("City_" + getLanguageKey(country_code)));
            c.put("City_Code", city.getString("City_Code"));
            c.put("IDCode", city.getString("IDCode"));
            
            filter.put(c);
        }
        
        return filter;
    }
    
    public JSONArray getStations(String country_code, String city_code){
        JSONArray filter = new JSONArray();
        for (int i = 0; i < Stations.length(); i++) {
            JSONObject station = Stations.getJSONObject(i);
            
            if(station.getString("City_Code").equals(city_code)){
                JSONObject s = new JSONObject();
                s.put("Station_Code", station.getString("Station_Code"));
                s.put("Station_Name",station.getString("Station_" + getLanguageKey(country_code)));
                s.put("City_Code", station.getString("City_Code"));
                s.put("Station_Order", station.getInt("Station_Order"));
                s.put("IDCode", station.getString("IDCode"));
                filter.put(s);
            }
        }
        return filter;
    }
}
