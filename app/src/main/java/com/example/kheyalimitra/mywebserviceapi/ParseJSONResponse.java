package com.example.kheyalimitra.mywebserviceapi;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KheyaliMitra on 3/22/2015.
 */
public class ParseJSONResponse {
    //Child hierarchy
    public Map<String, List<String>> AdventureWorksDomainDetails;
    //Measure details
    public ArrayList<String> AdventureWorksMeasureDetails ;

    /***
     * Parse JSON response for domains
     * @param domains
     * @return
     * @throws ParseException
     */
    public Map<String,List<String>>ParseDomainRecords(String domains) throws ParseException {
        AdventureWorksDomainDetails = new LinkedHashMap<String, List<String>>();
        JSONParser parser=new JSONParser();
        Object obj=parser.parse(domains);
        HashMap<String,Object> domainDetails = (HashMap<String,Object>)obj;
        Iterator it = domainDetails.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            List<String> childrenList=  new ArrayList<String>();
            HashMap<String,Object> innerchild = (HashMap<String,Object>)pair.getValue();
            Iterator inner = innerchild.entrySet().iterator();
            while(inner.hasNext())
            {
                HashMap.Entry child = (HashMap.Entry)inner.next();
                String []value = child.getValue().toString().substring(1,child.getValue().toString().length()-2).split(",");
                childrenList.add(child.getKey().toString());

            }
            AdventureWorksDomainDetails.put(pair.getKey().toString(),childrenList);
            inner.remove();
            it.remove();
        }
        return AdventureWorksDomainDetails;
    }

    /***
     * Parse JSON response for measures
     * @param measures
     * @return
     */
    public ArrayList<String> ParseMeasureResponse(String measures) {
        AdventureWorksMeasureDetails =  new ArrayList<String>();
        String commaseparations[] = measures.trim().split(",");
        for (int i=0;i<commaseparations.length;i++)
        {
            String splits[]= commaseparations[i].split("0");
            AdventureWorksMeasureDetails.add(splits[1].replace("|","").replace("\"","").replace("]",""));
        }
        return  AdventureWorksMeasureDetails;
    }

}
