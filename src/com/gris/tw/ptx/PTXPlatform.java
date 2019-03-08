package com.gris.tw.ptx;


import com.gris.tw.HttpsGetClient;
import com.gris.tw.HttpsGetClient;
import com.gris.tw.HttpsPostProxyClient;
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
public class PTXPlatform extends HttpsPostProxyClient {
    public PTXPlatform(String url){
        super(url);
    }

    public String getData() throws Exception {
        return super.fetchData();
    }
}
