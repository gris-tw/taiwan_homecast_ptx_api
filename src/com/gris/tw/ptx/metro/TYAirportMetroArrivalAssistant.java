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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class TYAirportMetroArrivalAssistant extends DataHandler{
    
    private JSONArray stations;
    private Date estimateTime;
    private int trainType;
    
    //please considering the terminal station has two way to same destination.
    public TYAirportMetroArrivalAssistant(TYAirportMetroStationDestSelector tmsds) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyManagementException, IOException{
        super();
        
        SimpleDateFormat sd = new SimpleDateFormat("EEEE", Locale.US); // the day of the week spelled out completely
        String weekDayFormat = sd.format(new Date());
        
        super.setPTXPURL(("https://ptx.transportdata.tw/MOTC/v2/Rail/Metro/StationTimeTable/TYMC" + "?$filter=StationID eq " + URLEncoder.encode("'" + tmsds.stationID + "'", "UTF-8") + " and DestinationStaionID eq "+ URLEncoder.encode("'" + tmsds.destID + "'","UTF-8") +" and LineID eq "+ URLEncoder.encode("'" + tmsds.mLineID + "'", "UTF-8") +" and ServiceDays/" + weekDayFormat + "&$top=30&$format=JSON").replaceAll(" ", "%20"));
        super.fetchPTXPData();
        
        this.trainType = tmsds.TrainType;
        stations = new JSONArray(super.Data);
    }
    
    private void handleTerminalStationTime(){
        int autoTimeoffset = 15; //add 15 min time to show train info;
        JSONArray timetable = new JSONArray();
        for (int i = 0; i < stations.length(); i++) {
            JSONArray ts = stations.getJSONObject(i).getJSONArray("Timetables");
            for (int j = 0; j < ts.length(); j++) {
                JSONObject t = ts.getJSONObject(j);
                
                System.out.println(t);
                
                if(this.trainType != -1 && (t.getInt("TrainType") != this.trainType)){
                    continue;
                }
                
                JSONObject filter = new JSONObject();
                filter.put("ArrivalTime", t.getString("ArrivalTime"));
                filter.put("DepartureTime", t.getString("DepartureTime"));
                filter.put("TrainType", t.getInt("TrainType"));

                timetable.put(filter);
            }
        }
        
        //search nearby time
        for (int i = 0; i < timetable.length(); i++) {
            JSONObject train = timetable.getJSONObject(i);
            
            String TrainArrivalTime = train.getString("ArrivalTime");
            String splitDate[] = TrainArrivalTime.split(":");
            Date arrivalTime = new Date();
            arrivalTime.setHours(Integer.parseInt(splitDate[0]));
            arrivalTime.setMinutes(Integer.parseInt(splitDate[1]));
            arrivalTime.setSeconds(0);
            
            Date now = new Date();
            now.setMinutes(now.getMinutes() + autoTimeoffset);
            if(arrivalTime.after(now)){
                this.estimateTime = arrivalTime;
                return;
            }
        }
    }
    
    public Date getEstimateTime(){
        handleTerminalStationTime();
        return estimateTime;
    }
    
    public Date refreshTime() throws NoSuchAlgorithmException, KeyManagementException, IOException{
        super.fetchPTXPData();
        return getEstimateTime();
    }
}
