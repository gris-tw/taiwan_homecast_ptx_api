
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;

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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //https://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/1406/to/1238/2019-02-22?$orderby=OriginStopTime%2FArrivalTime&$format=JSON
        
        Date now = new Date();
        now.setHours(12);
        now.setMinutes(14);
        
        TrainArrivalAssistant taa = new TrainArrivalAssistant("1406","1238","2019-02-23");
        taa.refreshTimeData();
        System.out.println("is train left: " + taa.isTrainLeft("136",0));
        System.out.println("is train left: " + taa.isTrainLeft("136",30));
        
        System.out.println("--------");
        if(taa.hasNextTrain(0, 0)){
            JSONObject train = taa.getNerbyTrain(0, 0);
            System.out.println(train.getJSONObject("DailyTrainInfo").getString("TrainNo"));
            System.out.println(taa.getTrainArrivalTime(train));
        }else{
            System.out.println("no any train");
        }
        System.out.println("--------");
        
        if(taa.hasNextTrain(0, 1)){
            JSONObject train = taa.getNerbyTrain(0, 1);
            System.out.println(train.getJSONObject("DailyTrainInfo").getString("TrainNo"));
            System.out.println(taa.getTrainArrivalTime(train));
        }else{
            System.out.println("no any train");
        }
        
        System.out.println("--------");
        
        if(taa.hasNextTrain(0, 49)){
            JSONObject train = taa.getNerbyTrain(0, 49);
            System.out.println(train.getJSONObject("DailyTrainInfo").getString("TrainNo"));
            System.out.println(taa.getTrainArrivalTime(train));
        }else{
            System.out.println("no any train");
        }
        
        System.out.println("-----------------");
        NationalStationInfo nsi = new NationalStationInfo();
        Iterator it = nsi.CountryCodeSupport().keySet().iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("---");
        JSONArray cities = nsi.getCities("TC");
        for (int i = 0; i < cities.length(); i++) {
            System.out.println(cities.getJSONObject(i));
        }
        System.out.println("================");
        JSONArray stations = nsi.getStations("TC", "10");
        for (int i = 0; i < stations.length(); i++) {
            System.out.println(stations.getJSONObject(i));
        }
        
        
    }
    
    
    
}
