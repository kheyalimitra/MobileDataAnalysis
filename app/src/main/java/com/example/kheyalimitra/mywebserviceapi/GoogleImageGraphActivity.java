package com.example.kheyalimitra.mywebserviceapi;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebSettings;
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
        //new code:
        _generateTable();
        _generateChart();
        ///////
        /*TableLayout tl = (TableLayout)findViewById(R.id.TableData);
        _processResponseForTable(tl);
        String GraphURL = _processResponseForChart();//"https://chart.googleapis.com/chart?chs=320x200&cht=p&chd=t:15,26,51,61&chxt=y,x&chxl=1:|Accesory|Bike|Tele|Chain&chbh=35&chtt=Truiton%27s+Performance&chds=0,300";
        Log.v("GraphURL :", GraphURL);
        WebView webview = (WebView) findViewById(R.id.graphView);
        WebView webviewBar = (WebView) findViewById(R.id.graphViewBar);
        webview.loadUrl(GraphURL);
        webviewBar.loadUrl(_processResponseForBarChart());*/


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
            Iterator it =DimensionTree.QueryResponse.iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
               value +=pair.getValue()+",";
               key+="|"+pair.getKey();
            }
            value=value.substring(0,value.lastIndexOf(","));
            GraphURL = "https://chart.googleapis.com/chart?chs=200x100&cht=p3&chd=t:"+value+"&chxt=y,x&chxl=1:"+key+"&chbh=35&chtt=Analysis&chds=0,300";
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
            Iterator it =DimensionTree.QueryResponse.iterator();
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

    private void _generateChart( )
    {
        WebView webview = (WebView) findViewById(R.id.graphView);

        String content = "<html>  \n" +
                "<head>   \n" +
                " <script type=\"text/javascript\" src=\"https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['bar']}]}\"></script> \n" +
                "   <script type=\"text/javascript\">     \n" +
                " google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});   \n" +
                "   google.setOnLoadCallback(drawTitleSubtitle);     \n" +
                " function drawTitleSubtitle() {        \n" +
                "var data =new google.visualization.DataTable();"
                +           ServiceCallThread.UserQueryResponse
                + "        var options = {       \n" +
                "  chart: {\n" +
                "\n" +
                "          title: 'Analysis'\n" +
                " },         \n" +
                " hAxis: {\n" +
                "viewWindow: {\n" +
                "            min: [7, 50, 0],\n" +
                "            max: [17, 50, 0]\n" +
                "          }\n" +
                "        }\n" +
                "};\n" +
                "\n" +
                "\n" +
                "        var material = new google.charts.Bar(document.getElementById('chart_div'));\n" +
                " material.draw(data, options);   \n" +
                "   }    </script> \n" +
                " </head> \n" +
                " <body>    \n" +
                "<div id=\"chart_div\" style=\"width: 400px; height: 200px;\"></div>  </body></html>";

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.requestFocusFromTouch();
        webview.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
    }
    private void _generateTable()
    {
        WebView tableView = (WebView) findViewById(R.id.webTableView);
        String content = "<html>  \n" +
                "<head>    \n" +
                "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['table']}]}\"></script>\n" +
                "\n" +
                "       \n" +
                "<script type=\"text/javascript\">     \n" +
                "  \n" +
                " google.setOnLoadCallback(drawTable);      \n" +
                "function drawTable() {        \n" +
                "var data = new google.visualization.DataTable();"
                +           ServiceCallThread.UserQueryResponse+
        "var table = new google.visualization.Table(document.getElementById('table_div'));"+
        "table.draw(data, {showRowNumber: true});"+
   " }    </script>   </head>"+
    "<body>     <div id=\"table_div\" style=\"width: 400px; height: 250px;\"></div>     </body></html>";

        WebSettings webSettings = tableView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        tableView.requestFocusFromTouch();
        tableView.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
    }
    private void _processResponseForTable(TableLayout tableLayout)
    {


        try
        {
           /* Iterator it =DimensionTree.QueryResponse.iterator();
            //TableRow tableRow1 = (TableRow) findViewById(R.id.tableRow1);
            /tableRow1.setBackgroundColor(Color.LTGRAY);
            while (it.hasNext()) {

                Map<String,String> value = (Map<String,String>)it.next();
                Iterator itr =value.entrySet().iterator();
                TableRow tRow = new TableRow(MainActivity.MainContext);
                //TextView headingName = new TextView(MainActivity.MainContext);
                while(itr.hasNext())
                {

                    Map.Entry keyValue = (Map.Entry) itr.next();
                    String k =keyValue.getKey().toString();
                    int pos = 0;
                    String newK="";
                    pos=k.indexOf("&");
                    TextView heading = new TextView(MainActivity.MainContext);
                    heading.setGravity(Gravity.CENTER);
                    heading.setBackgroundColor(Color.LTGRAY);
                    if(pos!=0) {
                        newK = k.substring(pos + 2, k.length() - 1);
                        heading.setText(newK);
                    }
                    else
                        heading.setText(k);
                    String v = keyValue.getValue().toString();
                    TextView TextVal = new TextView(MainActivity.MainContext);
                    TextVal.setBackgroundColor(Color.WHITE);
                    TextVal.setGravity(Gravity.CENTER);
                    float val=0;
                    if(_isNumeric(v))
                    {
                        val =  Float.parseFloat(v);
                        int floorVal=Math.round(val);
                        TextVal.setText(String.valueOf(floorVal));
                    }
                    else
                        TextVal.setText(v);



                    tRow.addView(TextVal);
                    tableRow1.addView(heading);
                }

                tableLayout.addView(tRow);
            }


*/
        }
        catch (Exception e)
        {
            String s= e.getMessage();
        }

    }
    private boolean _isNumeric(String val)
    {
        try{
            double i =  Double.parseDouble(val);

            return true;
        }
        catch (Exception e)
        {
            return  false;
        }
    }
}