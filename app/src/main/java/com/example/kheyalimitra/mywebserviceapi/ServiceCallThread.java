package com.example.kheyalimitra.mywebserviceapi;
import org.ksoap2.serialization.SoapObject;
/**
 * Created by KheyaliMitra on 2/25/2015.
 */
public class ServiceCallThread extends  Thread{
    public  CallService cs;
    public static String Domains;
    public static String Measures;
    public void run(){
        try{
            cs=new CallService();
            String resp_Domain=cs.CallDimention();
            String resp_Measures = cs.CallMeasures();
            Domains=resp_Domain;
            Measures = resp_Measures;
        }catch(Exception ex)
        {
            String s = ex.getMessage();
        }
    }
}
