
import com.gris.tw.ptx.citybus.BusArrivalAssistant;
import com.gris.tw.ptx.citybus.BusRouteInfo;
import com.gris.tw.ptx.citybus.BusServiceCityInfo;
import com.gris.tw.ptx.citybus.BusStopInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mac
 */
public class BUS_TEST {
    public static void main(String[] args) throws Exception {
        
        String languageCode = "Zh_tw";
        
        BusServiceCityInfo bsci = new BusServiceCityInfo();
        System.out.println(bsci.getCities());
        System.out.println(bsci.cityIdMapping());
        
        System.out.println("-----------------------");
        BusRouteInfo bri = new BusRouteInfo(bsci.cityIdMapping().get(1));
        System.out.println(bri.getRouteName(languageCode));
        System.out.println(bri.searchRoute(languageCode, "168"));
        bri.setRouteUID("KHH2061");
        
        System.out.println("-----------------------");
        BusStopInfo bsi = new BusStopInfo(bri);
        System.out.println(bsi.getStopInformation());
        bsi.setStop("KHH12243",1);
                
        System.out.println("-----------------------");
        BusArrivalAssistant baa = new BusArrivalAssistant(bsi);
        System.out.println(baa.getStopInfo());
        System.out.println(baa.update());
        System.out.println( !baa.update().getBoolean("EstimateTimeAvaliable") ? "Bus is Not Avaliable" : "Now  Avaliable");
    }
}
