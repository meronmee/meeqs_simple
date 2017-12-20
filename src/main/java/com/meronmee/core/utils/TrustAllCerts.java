package com.meronmee.core.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 信任全部HTTPS证书的X509TrustManager
 * @author Meron
 *
 */
public class TrustAllCerts implements X509TrustManager {  
    @Override    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}  
    
    @Override    
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}  
    
    @Override    
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}    
}