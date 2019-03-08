package com.gris.tw;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hpcslag
 */
public class HttpsPostProxyClient{
    
    
    public static String ProxyURL = "http://localhost/ptx-service";
    
    public String target_url = "";
    
    private String responseData;
    
    private CloseableHttpClient httpclient;
    HttpPost proxy;
    
    public HttpsPostProxyClient(String url){
        this.target_url = url;
        
        httpclient = HttpClients.createDefault();
        proxy = new HttpPost(ProxyURL);
    }
    
    protected String fetchData() throws Exception{
        

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("apiString", this.target_url));
        
        try{
            proxy.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        }catch(Exception e){
            System.out.println("URL Encoding is not support.");
            throw new Exception("URL Encoding is not spport.");
        }

        //Execute and get the response.
        try{
            HttpResponse response = httpclient.execute(proxy);
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                return inputStreamReader(entity.getContent());
            }
        }catch(Exception e){
            System.out.println("Response is not working.");
            throw new Exception("Response is not working.");
        }
        
        throw new Exception("Fetch data not working.");
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
}
