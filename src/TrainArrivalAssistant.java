
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
public class TrainArrivalAssistant {
    
    private PTXPlatform ptxp;
    
    public String APPID = "";
    public String APPKey = "";
    
    public JSONArray timeData = null;
    
    public TrainArrivalAssistant(String origin,String dest, String date){
        String url = "https://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+ origin +"/to/"+ dest +"/"+ date +"?$orderby=OriginStopTime%2FArrivalTime&$format=JSON";
        ptxp = new PTXPlatform(url, APPID, APPKey);
        System.out.println("Warning: You Have to use refreshTimeData() to get train info.");
    }
    
    public void refreshTimeData() throws NoSuchAlgorithmException, KeyManagementException, IOException{
        timeData = new JSONArray(ptxp.getData());
    }
    
    public JSONObject getTrain(String train_id) throws Exception{
        JSONArray trainData = timeData;
        for (int i = 0; i < trainData.length(); i++) {
            JSONObject train = trainData.getJSONObject(i);
            String TrainArrivalTime = train.getJSONObject("OriginStopTime").getString("ArrivalTime");
            String splitDate[] = TrainArrivalTime.split(":");
            Date arrivalTime = new Date();
            arrivalTime.setHours(Integer.parseInt(splitDate[0]));
            arrivalTime.setMinutes(Integer.parseInt(splitDate[1]));
            
            
            if(train.getJSONObject("DailyTrainInfo").getString("TrainNo").equals(train_id)){
                return train;
            }
        }
        throw new Exception("Train Not Found.");
    }
    
    public Date getTrainArrivalTime(JSONObject train){
        String TrainArrivalTime = train.getJSONObject("OriginStopTime").getString("ArrivalTime");
        String splitDate[] = TrainArrivalTime.split(":");
        Date arrivalTime = new Date();
        arrivalTime.setHours(Integer.parseInt(splitDate[0]));
        arrivalTime.setMinutes(Integer.parseInt(splitDate[1]));
        return arrivalTime;
    }
    
    public boolean isTrainLeft(String train_id, int timeoffset){
        Date now = new Date();
        now.setMinutes(now.getMinutes() + timeoffset);
        try{
            JSONObject train = getTrain(train_id);
            if(getTrainArrivalTime(train).before(now)){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
    
    public boolean hasNextTrain(int timeoffset, int indexoffset){
        try{
            JSONObject train = getNerbyTrain(timeoffset, indexoffset);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    //getNerbyTimeTrainByOD(1406, 1238, "2019-02-22",0); //index offset is the trick to get next train after.
    public JSONObject getNerbyTrain(int timeoffset,int indexoffset) throws Exception{
        Date now = new Date();
        now.setMinutes(now.getMinutes() + timeoffset);
        
        JSONArray trainData = timeData;
        for (int i = 0; i < trainData.length(); i++) {
            String TrainArrivalTime = trainData.getJSONObject(i).getJSONObject("OriginStopTime").getString("ArrivalTime");
            String splitDate[] = TrainArrivalTime.split(":");
            Date arrivalTime = new Date();
            arrivalTime.setHours(Integer.parseInt(splitDate[0]));
            arrivalTime.setMinutes(Integer.parseInt(splitDate[1]));
            
            if(arrivalTime.after(now)){
                if(timeData.isNull(i + indexoffset)){
                    throw new Exception("Can't Find Any Next Train");
                }
                JSONObject train = new JSONObject(trainData.getJSONObject(i + indexoffset).toString());
                train.put("train_index", i);
                return train;
            }
        }
        throw new Exception("Can't Find Any Next Train");
    }
}
