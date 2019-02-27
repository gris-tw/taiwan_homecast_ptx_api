
import com.gris.tw.ptx.citybike.BikeInfo;
import com.gris.tw.ptx.citybike.BikeStopAssistant;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hpcslag
 */
public class BIKE_TEST {
   
    public static void main(String[] args) throws Exception {
        BikeInfo bi = new BikeInfo(BikeInfo.City.Taipei);
        
        System.out.println(bi.getStations());
        
        BikeStopAssistant bsa = new BikeStopAssistant(BikeInfo.City.Kaohsiung, "KHH1");
        System.out.println(bsa.getAvaliableBike());
    }
    
}
