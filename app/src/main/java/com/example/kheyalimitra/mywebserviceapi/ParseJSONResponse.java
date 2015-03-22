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
        //domain details hashmap instantiate
        AdventureWorksDomainDetails = new LinkedHashMap<String, List<String>>();
        //New JSON parser library
        JSONParser parser=new JSONParser();

        //Parse Domain string to get JSON object
        Object obj=parser.parse(domains);

        //Cast JSON object to HashMap
        HashMap<String,Object> domainDetails = (HashMap<String,Object>)obj;
        Iterator it = domainDetails.entrySet().iterator();
        //iterates all top level elements
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            List<String> childrenList=  new ArrayList<String>();

            //get HashMap object for inner child
            HashMap<String, Object> innerChild = (HashMap<String, Object>) pair.getValue();
            Iterator inner = innerChild.entrySet().iterator();
            while(inner.hasNext())
            {
                //get the child element
                HashMap.Entry child = (HashMap.Entry)inner.next();
                //get child string value
                String childString = child.getValue().toString();
                String[] value = childString.substring(1, childString.length() - 2).split(",");
                childrenList.add(child.getKey().toString());

            }

            //Add new child node to string list data structure
            AdventureWorksDomainDetails.put(pair.getKey().toString(), childrenList);
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
        String commaSeparations[] = measures.trim().split(",");
        for (int i = 0; i < commaSeparations.length; i++)
        {
            String splits[] = commaSeparations[i].split("0");
            AdventureWorksMeasureDetails.add(splits[1].replace("|","").replace("\"","").replace("]",""));
        }
        return  AdventureWorksMeasureDetails;
    }

}
