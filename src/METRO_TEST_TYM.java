
import com.gris.tw.ptx.metro.CityMetroInfo;
import com.gris.tw.ptx.metro.KHMetroArrivalAssistant;
import com.gris.tw.ptx.metro.MetroLineInfo;
import com.gris.tw.ptx.metro.MetroStationDestSelector;
import com.gris.tw.ptx.metro.MetroStationInfo;
import com.gris.tw.ptx.metro.TYAirportMetroArrivalAssistant;
import com.gris.tw.ptx.metro.TYAirportMetroStationDestSelector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mac
 */
public class METRO_TEST_TYM {
    
    public static void main(String[] args) throws Exception{
        String languageCode = "Zh_tw";
        
        CityMetroInfo cmi = new CityMetroInfo();
        System.out.println(cmi.getSupportCity(languageCode));
        
        System.out.println("---------------------");
        
        int city_code = 3;
        
        MetroLineInfo mli = new MetroLineInfo(city_code);
        System.out.println(mli.getLineData(languageCode));
        
        System.out.println("---------------------");
        
        String LineID = "A";
        MetroStationInfo msi = new MetroStationInfo(city_code, LineID);
        System.out.println(msi.getStationInfo(languageCode));
        
        System.out.println("---------------------");
        
        String StationID = "A2";
        TYAirportMetroStationDestSelector tmsds = new TYAirportMetroStationDestSelector(city_code, LineID, StationID);
        System.out.println(tmsds.getStationDest(languageCode));
        String Dest = "A1";
        tmsds.setTrainType(-1);
        tmsds.setDestination(Dest);
        
        System.out.println("---------------------");
        
        TYAirportMetroArrivalAssistant maa = new TYAirportMetroArrivalAssistant(tmsds); 
        System.out.println(maa.getEstimateTime());
        
    }
    
}
