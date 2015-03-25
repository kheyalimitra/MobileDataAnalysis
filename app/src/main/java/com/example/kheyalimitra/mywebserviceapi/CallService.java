package com.example.kheyalimitra.mywebserviceapi;
import android.util.Xml;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by KheyaliMitra on 2/24/2015.
 */
/////// GITHUB
public class CallService {
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public final String SOAP_ACTION_Dimen = "http://tempuri.org/Dimen";
    public final String SOAP_ACTION_Measure = "http://tempuri.org/Measures";
    public  final String SOAP_ACTION_Metadata2 = "http://tempuri.org/MetaData2";
    public  final String OPERATION_NAME_Domain = "Dimen";
    public  final String OPERATION_NAME_Measure = "Measures";
    public  final String OPERATION_NAME_Metadata2="MetaData2";
    public  final String WSDL_TARGET_NAMESPACE ="http://tempuri.org/";
    public  final String SOAP_ADDRESS ="http://webolap.cmpt.sfu.ca/ElaWebService/Service.asmx";

///////////////////////////////////////////
    public CallService()
    {

    }
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String CallDimention()throws XmlPullParserException, IOException
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_Domain);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        HttpURLConnection conn = null;
        URL url = new URL(SOAP_ADDRESS);
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(conn.getURL().toString());
        String  res= null;
        try {
            httpTransport.call(SOAP_ACTION_Dimen, envelope);
           res = envelope.getResponse().toString();

        }
        catch (Exception e) {
            int x= 0;

        }
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                InputStream in =  new InputStream() {
                    @Override
                    public int read() throws IOException {
                       return  0;
                    }
                };

        }
        catch (Exception exception)
        {
           String texts=exception.toString();
        }

        return res;
    }
    public String CallMeasures()throws XmlPullParserException, IOException
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_Measure);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
              SoapEnvelope.VER11);
        envelope.dotNet = true;
        HttpURLConnection conn = null;
        URL url = new URL(SOAP_ADDRESS);
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(conn.getURL().toString());//new HttpTransportSE(SOAP_ADDRESS);
        //java.util.Objects response;
        String  res= null;
        try {
              httpTransport.call(SOAP_ACTION_Measure, envelope);
              res = envelope.getResponse().toString();
           }
        catch (Exception e) {
            int x= 0;

        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            InputStream in =  new InputStream() {
                @Override
                public int read() throws IOException {
                    return  0;
                }
            };

        }
        catch (Exception exception)
        {
            String texts=exception.toString();
        }

        return res;
    }
    public String CallMetaData2(String selectedChild) throws XmlPullParserException, IOException
    {
        ///Soap object
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_Metadata2);
        ///passing parameter
        request.addProperty("Hierarchy",  selectedChild);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        HttpURLConnection conn = null;
        URL url = new URL(SOAP_ADDRESS);
        ///checking for  http or https
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(conn.getURL().toString());
        String  res= null;
        try {
            ///calling the web service
            httpTransport.call(SOAP_ACTION_Metadata2, envelope);
            res = envelope.getResponse().toString();
        }
        catch (Exception e) {
            int x= 0;

        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            InputStream in =  new InputStream() {
                @Override
                public int read() throws IOException {
                    return  0;
                }
            };

        }
        catch (Exception exception)
        {
            String texts=exception.toString();
        }

        return res;
    }
}
