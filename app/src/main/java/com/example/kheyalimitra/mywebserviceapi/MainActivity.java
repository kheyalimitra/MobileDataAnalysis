package com.example.kheyalimitra.mywebserviceapi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;


public class MainActivity extends Activity {
    public static String domains="";
    public static String measures="";
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> AdventureWorksDomainDetails;
    ArrayList<String> AdventureWorksMeasureDetails ;
    ExpandableListView expListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.getbtn);
        final AlertDialog ad=new AlertDialog.Builder(this).create();
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this);
        final Activity  activityObj =  this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //isClick = true;
                domains="START";
                measures = "START";
                ServiceCallThread sthread =  new ServiceCallThread();
                try {
                    sthread.join();
                    sthread.start();
                    while(domains=="START" || measures=="START") {
                        try {
                            Thread.sleep(10);
                        }catch(Exception ex) {
                        }

                    }
                    TextView domainView=(TextView)findViewById(R.id.webServiceText);
                    ListView mView =(ListView)findViewById(R.id.measurelistView) ;

                    try
                    {
                        AdventureWorksDomainDetails = new LinkedHashMap<String, List<String>>();
                        groupList = new ArrayList<String>();
                        AdventureWorksMeasureDetails =  new ArrayList<String>();
                       ArrayList<String> animalsNameList = new ArrayList<String>();
                        JSONObject jObject  ;
                        jObject = new JSONObject(domains.trim());
                        Iterator<?> keys = jObject.keys();
                        while( keys.hasNext() )
                        {
                                 String key = (String)keys.next();
                                 if(key!=null)
                                 {
                                     groupList.add(key);
                                     List<String> children =  new ArrayList<String>();
                                     JSONObject childObj = jObject.getJSONObject(key);
                                     Iterator<?> childKeys = childObj.keys();
                                     while(childKeys.hasNext())
                                     {
                                         String chKey= (String)childKeys.next();
                                         if(chKey!=null)
                                         {
                                             children.add(chKey);
                                         }
                                     }
                                     createCollection(key,children);
                                 }

                        }
                        String commaseparations[] = measures.trim().split(",");
                        for (int i=0;i<commaseparations.length;i++)
                            {
                                String splits[]= commaseparations[i].split("0");
                                  AdventureWorksMeasureDetails.add(splits[1].replace("|","").replace("\"","").replace("]",""));
                            }

                        /*JSONObject mObject = new JSONObject(measures.trim());
                         Iterator<?> measureKey =  mObject.keys();
                        while(measureKey.hasNext())
                            {
                                String k = (String)measureKey.next();
                                if(k!=null)
                                    {
                                        AdventureWorksMeasureDetails.add(k);
                                    }
                            }
                             ;   */
                                    ArrayAdapter<String> arrayAdapter =
                                                     new ArrayAdapter<String>(activityObj,android.R.layout.simple_list_item_1, AdventureWorksMeasureDetails ); // mView.setAdapter(arrayAdapter);

                             mView.setAdapter(arrayAdapter);




                       // mView.addView();
                     expListAdapter.AdvWorkDetails = AdventureWorksDomainDetails;
                        expListAdapter.dimentions =  groupList;
 expListView = (ExpandableListView) findViewById(R.id.expandableListView);
          TextView tView =  (TextView) findViewById(R.id.textView);
          btn.setVisibility(View.GONE);
          tView.setVisibility(View.GONE);

     expListView.setAdapter(expListAdapter);
     expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

         public boolean onChildClick(ExpandableListView parent, View v,
                                     int groupPosition, int childPosition, long id) {
             final String selected = (String) expListAdapter.getChild(
                     groupPosition, childPosition);
             Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                     .show();

             return true;
         }
     });


                        //ed1.setText(AdventureWorksDetails.toString());

                    }
                    catch(Exception ex)
                    {
                        domainView.setText("Error!");
                    }
                }
                catch (Exception e)
                {
                    String s =  e.getMessage();
                }

              }

        }

        );
               // if(isClick)
                  //  btn.setVisibility(View.INVISIBLE);
    }


    private void createCollection(String key, List<String> children) {
        // preparing laptops collection(child)
        for (String details : groupList) {
            if (details.equals(key)) {
                loadChild(children);
            }

            AdventureWorksDomainDetails.put(details, childList);
        }
    }
    private void loadChild(List<String> childrens) {
        childList = new ArrayList<String>();
        for (String item : childrens)
            childList.add(item);
    }
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
