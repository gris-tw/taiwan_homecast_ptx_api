
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hpcslag
 */
public class HttpsClient {
    
    public String target_url = "";
    
    private String responseData;
    private HttpGet request;
    
    public HttpsClient(String url){
        this.target_url = url;
        request = new HttpGet(target_url);
    }
    
    public String getResponseData(){
        return this.responseData;
    }
    
    protected void SetHeader(String key, String value){
        request.setHeader(key, value);
    }
    
    
    protected String fetchURL() throws NoSuchAlgorithmException, KeyManagementException, IOException{
        //String https_url = "https://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/1406/to/1238/2019-02-22?$orderby=OriginStopTime%2FArrivalTime&$format=JSON";
        
        CloseableHttpResponse response = skipSSLBuilder().execute(request);
        String body = inputStreamReader(response.getEntity().getContent());
        this.responseData = body;
        return body;
   }
    
    private String inputStreamReader(InputStream content) throws IOException{
        BufferedReader br = 
            new BufferedReader(
                    new InputStreamReader(content));

        String body = "";
        String input;	
        while ((input = br.readLine()) != null){
          body += input;
        }
        br.close();
        return body;
    }
    
    private CloseableHttpClient skipSSLBuilder() throws KeyManagementException, NoSuchAlgorithmException{
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }

            }
        };
        
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, null);

        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
        
        return httpclient;
    }
    
    protected static String HMAC_SHA1_Signature(String xData, String AppKey) throws java.security.SignatureException {
            try {
                    final Base64.Encoder encoder = Base64.getEncoder();
                    // get an hmac_sha1 key from the raw key bytes
                    SecretKeySpec signingKey = new SecretKeySpec(AppKey.getBytes("UTF-8"),"HmacSHA1");

                    // get an hmac_sha1 Mac instance and initialize with the signing key
                    Mac mac = Mac.getInstance("HmacSHA1");
                    mac.init(signingKey);

                    // compute the hmac on input data bytes
                    byte[] rawHmac = mac.doFinal(xData.getBytes("UTF-8"));
                    String result = encoder.encodeToString(rawHmac);
                    return result;

            } catch (Exception e) {
                    throw new SignatureException("Failed to generate HMAC : "+ e.getMessage());
            }
    }
}
