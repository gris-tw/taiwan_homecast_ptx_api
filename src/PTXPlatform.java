
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hpcslag
 */
public class PTXPlatform extends HttpsClient {
    
    private String APIUrl = ""; // "https://ptx.transportdata.tw/MOTC/APIs/v2/Rail/TRA/Station?$top=10&$format=JSON";
    //申請的APPID
    //（FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF 為 Guest 帳號，以IP作為API呼叫限制，請替換為註冊的APPID & APPKey）
    private String APPID = "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
    //申請的APPKey
    private String APPKey = "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
    
    public PTXPlatform(String url, String APPID, String APPKey){
        super(url);
        this.APIUrl = url;
        if(!APPID.isEmpty() && !APPKey.isEmpty()){
            this.APPID = APPID;
            this.APPKey = APPKey;
            return;
        }
        System.out.println("You're using Guest Mode to Fetch PTX API.");
    }
    
    private String xdate;
    private String sAuthGenerator(){
        //取得當下的UTC時間，Java8有提供時間格式DateTimeFormatter.RFC_1123_DATE_TIME
        //但是格式與C#有一點不同，所以只能自行定義
        xdate = getServerTime();
        String SignDate = "x-date: " + xdate;

        String Signature = "";
        try {
            //取得加密簽章
            Signature = HMAC_SHA1_Signature(SignDate, APPKey);
        } catch (SignatureException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("Signature :" + Signature);
        String sAuth = "hmac username=\"" + APPID + "\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"" + Signature + "\"";
        return sAuth;
    }

    protected String getData() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        SetHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) ...");
        SetHeader("Authorization", sAuthGenerator());
        SetHeader("x-date",xdate);
        SetHeader("Accept-Encoding","gzip");
        return fetchURL();
    }

    private String gzipReader(InputStream content) throws IOException {
        //將InputStream轉換為Byte
        InputStream inputStream = content;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buff)) != -1) {
            bao.write(buff, 0, bytesRead);
        }

        //解開GZIP
        ByteArrayInputStream bais = new ByteArrayInputStream(bao.toByteArray());
        GZIPInputStream gzis = new GZIPInputStream(bais);
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        //讀取回傳資料
        String line, response = "";
        while ((line = in.readLine()) != null) {
            response += (line + "\n");
        }
        return response;
    }

    private String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}
