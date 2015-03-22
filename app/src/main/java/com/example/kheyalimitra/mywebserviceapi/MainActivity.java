package com.example.kheyalimitra.mywebserviceapi;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity  extends Activity{

    //Child hiearchy
    public Map<String, List<String>> AdventureWorksDomainDetails;
    //Measure details
    public ArrayList<String> AdventureWorksMeasureDetails;
    //Top level names of Domain
    List<String> groupList;
    //List view object
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.getbtn);
        ///Display data in Text view and List View

        final AlertDialog ad = new AlertDialog.Builder(this).create();
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this);
        final Activity activityObj = this;

        btn.setOnClickListener(new View.OnClickListener() {

                                   /**
                                    * Populates JSON data into view
                                    */
                                   @Override
                                   public void onClick(View arg0) {

                                       try {
                                           ParseJSONResponse parse = new ParseJSONResponse();
                                           _startServiceThread();
                                           AdventureWorksDomainDetails = parse.ParseDomainRecords(ServiceCallThread.Domains);
                                           AdventureWorksMeasureDetails = parse.ParseMeasureResponse(ServiceCallThread.Measures);
                                           groupList = _populateTopLevelHierarchy();
                                           ///parse webservice response and populate HashMap List
                                           _populateJSONResponseToUI(activityObj, expListAdapter, btn);
                                       } catch (Exception e) {
                                           String s = e.getMessage();
                                       }

                                   }
                               }
        );

    }


    /**
     * Start Service thread
     */
    private void _startServiceThread(){

        //Initialize domain and measure as START
        //After valid population of both the variables, thread will come out of sleep mode

        ServiceCallThread.Domains = "START";
        ServiceCallThread.Measures = "START";
        ServiceCallThread sthread = new ServiceCallThread();
        try {
            sthread.join();
            sthread.start();
            while (ServiceCallThread.Domains == "START" || ServiceCallThread.Measures == "START") {
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                    throw new Exception(ex.getMessage());
                }
            }
        } catch (Exception e) {

        }
    }

    /***
     * Populates top level hierarchy of domain
     * @return
     */
    private ArrayList<String> _populateTopLevelHierarchy()
    {
        ArrayList<String> list  =new ArrayList<String>();
        Map<String,List<String>> domainDetails = AdventureWorksDomainDetails;
        Iterator it = domainDetails.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            list.add(pair.getKey().toString());
            it.remove();
        }
        return  list;
    }

    /***
     * Displays the records in Expandable List adapter and List view
     * @param activityObj
     * @param expListAdapter
     * @param btn
     */
    private void _populateJSONResponseToUI(Activity activityObj, final ExpandableListAdapter expListAdapter, Button btn) {

        TextView domainView = (TextView) findViewById(R.id.webServiceText);
        ListView mView = (ListView) findViewById(R.id.measurelistView);
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        try
        {
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(activityObj,android.R.layout.simple_list_item_1, AdventureWorksMeasureDetails ); // mView.setAdapter(arrayAdapter);
            mView.setAdapter(arrayAdapter);
            expListAdapter.AdvWorkDetails = AdventureWorksDomainDetails;
            expListAdapter.dimentions =  groupList;
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
        }
        catch(Exception ex)
        {
            domainView.setText("Error!Please Try Again.");
        }
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
