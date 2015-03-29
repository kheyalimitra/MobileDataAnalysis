package com.example.kheyalimitra.mywebserviceapi;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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
    public ArrayList<String> AdventureWorksHierarchyDetails ;
    public ArrayList<String> AdventureWorksAnalysisResponse;
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
        Map<String,Object> domainDetails = (Map<String,Object>)obj;
        Iterator it = domainDetails.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<String> childrenList=  new ArrayList<String>();
            Map<String,Object> innerchild = (Map<String,Object>)pair.getValue();
            Iterator inner = innerchild.entrySet().iterator();
            while(inner.hasNext())
            {
                Map.Entry child = (Map.Entry)inner.next();
                String []value = child.getValue().toString().substring(1,child.getValue().toString().length()-2).split(",");
                childrenList.add(child.getKey().toString());

            }
            Collections.sort(childrenList);
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
    public ArrayList<String> ParseMeasureResponse(String measures) throws ParseException{
        AdventureWorksMeasureDetails =  new ArrayList<String>();
        JSONParser parser=new JSONParser();
        Object obj=parser.parse(measures);
        ArrayList<String> measureDetails = (ArrayList<String>)obj;
        Iterator it = measureDetails.iterator();
        while(it.hasNext())
        {
            String[] modifiedVal  = it.next().toString().split("0");
            AdventureWorksMeasureDetails.add(modifiedVal[1].replace("|","").replace("\"","").replace("]",""));
            it.remove();
        }
        Collections.sort(AdventureWorksMeasureDetails);
        return  AdventureWorksMeasureDetails;
    }
    public ArrayList<String> ParseHierarchy(String hierarchy) throws ParseException
    {
        AdventureWorksHierarchyDetails =  new ArrayList<String>();
        JSONParser parser=new JSONParser();
        Object obj=parser.parse(hierarchy);
        ArrayList<String> measureDetails = (ArrayList<String>)obj;
        Iterator it = measureDetails.iterator();
        while(it.hasNext())
        {
            String value = it.next().toString();
            int subStringEndPos  = value.indexOf("|",2);
            String child = value.substring(2,subStringEndPos);
            AdventureWorksHierarchyDetails.add(child);
            it.remove();
        }
        Collections.sort(AdventureWorksHierarchyDetails);

        return  AdventureWorksHierarchyDetails;
    }
    public ArrayList<String> ParseUserQuery(String query)
    {
        AdventureWorksAnalysisResponse =  new ArrayList<String>();

        return  AdventureWorksAnalysisResponse;

    }

}
