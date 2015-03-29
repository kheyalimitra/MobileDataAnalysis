package com.example.kheyalimitra.mywebserviceapi;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GoogleImageGraphActivity extends Activity {
    //private Map<String,String> QueryResponse;
  /*  public GoogleImageGraphActivity(Map<String,String>queryResponse)
    {
        this.QueryResponse = queryResponse;
    }*/
    private DimensionTree dTree;
    public GoogleImageGraphActivity()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        TableLayout tl = (TableLayout)findViewById(R.id.TableData);
        _processResponseForTable(tl);
        String GraphURL = _processResponseForChart();//"https://chart.googleapis.com/chart?chs=320x200&cht=p&chd=t:15,26,51,61&chxt=y,x&chxl=1:|Accesory|Bike|Tele|Chain&chbh=35&chtt=Truiton%27s+Performance&chds=0,300";
        Log.v("GraphURL :", GraphURL);
        WebView webview = (WebView) findViewById(R.id.graphView);
        WebView webviewBar = (WebView) findViewById(R.id.graphViewBar);
        webview.loadUrl(GraphURL);
        webviewBar.loadUrl(_processResponseForBarChart());


    }
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    private String _processResponseForChart()
    {
        String GraphURL="";
        String value="";
        String key="";
        try
        {
            Iterator it =DimensionTree.QueryResponse.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
               value +=pair.getValue()+",";
               key+="|"+pair.getKey();
            }
            value=value.substring(0,value.lastIndexOf(","));
            String val=value.replace("/[!@#$%^&*+-_,~?']/g", "");
            GraphURL = "https://chart.googleapis.com/chart?chs=200x100&cht=p3&chd=t:"+val+"&chxt=y,x&chxl=1:"+key+"&chbh=35&chtt=Analysis&chds=0,300";
            return GraphURL;

        }
        catch (Exception e)
        {
            String s= e.getMessage();
        }
        return  GraphURL;
    }

    private String _processResponseForBarChart()
    {
        String GraphURL="";
        String value="";
        String key="";
        try
        {
            Iterator it =DimensionTree.QueryResponse.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                value +=pair.getValue()+",";
                key+="|"+pair.getKey();
            }
            value=value.substring(0,value.lastIndexOf(","));
            GraphURL = "https://chart.googleapis.com/chart?chs=320x200&cht=bvs&chd=t:"+value+"&chxt=y,x&chxl=1:"+key+"&chbh=35&chds=a";
            return GraphURL;

        }
        catch (Exception e)
        {
            String s= e.getMessage();
        }
        return  GraphURL;
    }

    private void _processResponseForTable(TableLayout tableLayout)
    {


        try
        {
            Iterator it =DimensionTree.QueryResponse.entrySet().iterator();

            TableRow tableRow1 = (TableRow) findViewById(R.id.tableRow1);
            tableRow1.setBackgroundColor(Color.LTGRAY);
            TableRow tableRow2 = (TableRow) findViewById(R.id.tableRow2);
            tableRow2.setBackgroundColor(Color.WHITE);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String value = (String)pair.getValue();
                String key =(String)pair.getKey();

                TextView val = new TextView(MainActivity.MainContext);
                val.setText((value));
                val.setBackgroundColor(Color.WHITE);
                val.setGravity(Gravity.CENTER);
                tableRow2.addView(val);
                TextView heading = new TextView(MainActivity.MainContext);
                heading.setText((key));
                heading.setBackgroundColor(Color.LTGRAY);
                heading.setGravity(Gravity.CENTER);
                tableRow1.addView(heading);
            }


        }
        catch (Exception e)
        {
            String s= e.getMessage();
        }

    }
}