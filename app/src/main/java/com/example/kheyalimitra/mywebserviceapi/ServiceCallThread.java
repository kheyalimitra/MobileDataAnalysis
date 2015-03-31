package com.example.kheyalimitra.mywebserviceapi;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by KheyaliMitra on 2/25/2015.
 */
public class ServiceCallThread extends  Thread{
    public  CallService cs;
    public static String Domains;
    public static String Measures;
    public static String Hierarchy;
    public static String UserQueryResponse;
    boolean isHierarchyCall = false;
    public  String Parameter;
    public ServiceCallThread(String param)
    {
        Parameter = param;
        if(!param.equals("START"))
            isHierarchyCall=true;

    }
    public void run() {
        cs = new CallService();
      if(!isHierarchyCall) {
          try {
              String resp_Domain = cs.CallDimention();
              String resp_Measures = cs.CallMeasures();
              Domains = resp_Domain;
              Measures = resp_Measures;
          } catch (Exception ex) {
              String s = ex.getMessage();
          }
      }
        else
      {
          try {
              // this is call to user query
              if(Parameter.toUpperCase().contains("FROM [ADVENTURE WORKS]"))
              {
                  UserQueryResponse =  cs.CallFetchDataFromAdventureWorks(Parameter);

              }
              else {
                  // this is the call to generate deep level hierarchy
                    String res_Hier = cs.CallMetaData2(Parameter);
                    Hierarchy = res_Hier;
              }

          }
          catch (Exception e)
          {
            String s = e.getMessage();
          }
      }
    }
}
