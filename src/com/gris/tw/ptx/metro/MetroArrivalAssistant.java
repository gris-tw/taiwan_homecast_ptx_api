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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class MetroArrivalAssistant extends DataHandler{
    
    private JSONArray stations;
    private int estimateTime;
    
    //please considering the terminal station has two way to same destination.
    public MetroArrivalAssistant(MetroStationDestSelector msds) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyManagementException, IOException{
        super();
        super.setPTXPURL(("https://ptx.transportdata.tw/MOTC/v2/Rail/Metro/LiveBoard/" + super.getCityCodeTranslation().get(msds.city_code) + "?$filter=StationID eq " + URLEncoder.encode("'" + msds.stationID + "'", "UTF-8") + " and DestinationStaionID eq "+ URLEncoder.encode("'" + msds.destID + "'","UTF-8") +" and LineID eq "+ URLEncoder.encode("'" + msds.mLineID + "'", "UTF-8") +"&$top=30&$format=JSON").replaceAll(" ", "%20"));
        super.fetchPTXPData();
        
        stations = new JSONArray(super.Data);
    }
    
    private void handleTerminalStationTime(){
        if(stations.length() > 1){
            //compare EstimateTime
            int time[] = new int[stations.length()] ;
            for (int i = 0; i < stations.length(); i++) {
                time[i] = stations.getJSONObject(i).getInt("EstimateTime");
            }
            bubbleSort(time);
            this.estimateTime = time[0];
        }else{
            this.estimateTime = stations.getJSONObject(0).getInt("EstimateTime");
        }
    }
    
    private void bubbleSort(int arr[]) 
    { 
        int n = arr.length; 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (arr[j] > arr[j+1]) 
                { 
                    // swap arr[j+1] and arr[i] 
                    int temp = arr[j]; 
                    arr[j] = arr[j+1]; 
                    arr[j+1] = temp; 
                } 
    } 
    
    public int getEstimateTime(){
        handleTerminalStationTime();
        return estimateTime;
    }
    
    public int refreshTime() throws NoSuchAlgorithmException, KeyManagementException, IOException{
        super.fetchPTXPData();
        return getEstimateTime();
    }
}
