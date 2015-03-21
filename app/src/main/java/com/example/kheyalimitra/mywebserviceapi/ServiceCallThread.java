package com.example.kheyalimitra.mywebserviceapi;
import org.ksoap2.serialization.SoapObject;
/**
 * Created by KheyaliMitra on 2/25/2015.
 */
public class ServiceCallThread extends  Thread{
    public  CallService cs;
    public void run(){
        try{
            cs=new CallService();
            String resp_Domain=cs.CallDimention();
            String resp_Measures = cs.CallMeasures();
            MainActivity.domains = resp_Domain;
            MainActivity.measures = resp_Measures;
        }catch(Exception ex)
        {MainActivity.domains=ex.toString();}
    }
}
