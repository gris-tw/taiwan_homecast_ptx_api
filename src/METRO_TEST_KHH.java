
import com.gris.tw.ptx.metro.CityMetroInfo;
import com.gris.tw.ptx.metro.KHMetroArrivalAssistant;
import com.gris.tw.ptx.metro.MetroLineInfo;
import com.gris.tw.ptx.metro.MetroStationDestSelector;
import com.gris.tw.ptx.metro.MetroStationInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mac
 */
public class METRO_TEST_KHH {
    
    public static void main(String[] args) throws Exception{
        String languageCode = "Zh_tw";
        
        CityMetroInfo cmi = new CityMetroInfo();
        System.out.println(cmi.getSupportCity(languageCode));
        
        System.out.println("---------------------");
        
        int city_code = 1;
        
        MetroLineInfo mli = new MetroLineInfo(city_code);
        System.out.println(mli.getLineData(languageCode));
        
        System.out.println("---------------------");
        
        String LineID = "O";
        MetroStationInfo msi = new MetroStationInfo(city_code, LineID);
        System.out.println(msi.getStationInfo(languageCode));
        
        System.out.println("---------------------");
        
        String StationID = "O1";
        MetroStationDestSelector msds = new MetroStationDestSelector(city_code, LineID, StationID);
        System.out.println(msds.getStationDest(languageCode));
        String Dest = "OT1";
        msds.setDestination(Dest);
        
        System.out.println("---------------------");
        
        KHMetroArrivalAssistant maa = new KHMetroArrivalAssistant(msds); 
        System.out.println(maa.getEstimateTime());
        
    }
    
}
