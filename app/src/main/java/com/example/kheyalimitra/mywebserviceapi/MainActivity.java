package com.example.kheyalimitra.mywebserviceapi;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.unnamed.b.atv.model.TreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;

import java.util.Map;
import java.util.Set;


public class MainActivity  extends ActionBarActivity { //extends Activity{

    //Top level names of Domain
    List<String> groupList;

    //Child hiearchy
    public Map<String, List<String>> AdventureWorksDomainDetails;

    //Measure details
    public ArrayList<String> AdventureWorksMeasureDetails ;
    public static Context MainContext;
    //List view object
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final Button btn = (Button) findViewById(R.id.getbtn);
        final LinkedHashMap<String, Class<?>> listItems = new LinkedHashMap<>();
        listItems.put("Get Dimensions and Measures", DimensionTree.class);

        //final TextView dimView = (TextView) findViewById(R.id.domainView);
        //final TextView measureView = (TextView) findViewById(R.id.measureView);
        //dimView.setVisibility(View.INVISIBLE);
        //measureView.setVisibility(View.INVISIBLE);
        ///Display data in Text view and List View
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        final Activity activityObj = this;
        MainContext = activityObj;
        //PopulateDetails details =  new PopulateDetails();

        //details.BindButtonClick(btn, expListAdapter, activityObj);


        final List<String> list = new ArrayList(listItems.keySet());
        final ListView listview = (ListView) findViewById(R.id.listview);
        final SimpleArrayAdapter adapter = new SimpleArrayAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //btn.setOnClickListener(new View.OnClickListener() {

                                   /**
                                    * Populates JSON data into view
                                    */
                                   @Override
                                  // public void onClick(View arg0) {
                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                       try {

                                           /*ParseJSONResponse parse = new ParseJSONResponse();
                                           _startServiceThread();
                                           AdventureWorksDomainDetails = parse.ParseDomainRecords(ServiceCallThread.Domains);
                                           AdventureWorksMeasureDetails = parse.ParseMeasureResponse(ServiceCallThread.Measures);*/
                                           //groupList = _populateTopLevelHierarchy();
                                           ///parse webservice response and populate HashMap List
                                          // _populateJSONResponseToUI(activityObj, btn);


                                           ///// new code for tree view

                                          // Class<?> clazz = listItems.values().toArray(new Class<?>[]{})[position];
                                           Intent i = new Intent(MainActivity.this, TreeViewActivity.class);
                                           //Intent androidView = new Intent(MainActivity.this,DimensionTree.class);
                                           Class<?> clazz = listItems.values().toArray(new Class<?>[]{})[position];
                                           i.putExtra(TreeViewActivity.View_PARAM,clazz );
                                           MainActivity.this.startActivity(i);
                                           //MainActivity.this.startActivity(androidView);
                                           ////////
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
    public void StartServiceThread(){

        //Initialize domain and measure as START
        //After valid population of both the variables, thread will come out of sleep mode

        ServiceCallThread.Domains = "START";
        ServiceCallThread.Measures = "START";
        ServiceCallThread sthread = new ServiceCallThread("START");
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
    //private ArrayList<String> _populateTopLevelHierarchy()
    public TreeNode PopulateTreeHierarchy()
    {
        TreeNode dRoot = new TreeNode("Dimension/Hierarchy:");
        TreeNode dimRoot = new TreeNode("");

        try {

            ArrayList<String> list = new ArrayList<String>();
            Map<String, List<String>> domainDetails = AdventureWorksDomainDetails;
            //Set<String> keys = AdventureWorksDomainDetails.keySet();
            //list.addAll(keys);
            //Collections.sort(list);
            /// populate tree nodes
            Iterator it = AdventureWorksDomainDetails.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TreeNode t = new TreeNode(pair.getKey());
                if(AdventureWorksDomainDetails.get(pair.getKey()).size()>1) {
                    List<String> innerchild = (List<String>) pair.getValue();
                    Iterator inner = innerchild.iterator();
                    while (inner.hasNext()) {
                        String child = (String) inner.next();
                        TreeNode c = new TreeNode(child);
                        t.addChild(c);

                    }
                }
                dRoot.addChild(t);
            }

        }
        catch (Exception e)
        {
                String s = e.getMessage();
        }
        dimRoot.addChild(dRoot);
        return  dimRoot;
    }

    public TreeNode PopulateMeasures()
    {
        TreeNode mRoot = new TreeNode("Measures:");
        TreeNode mesRoot = new TreeNode("");
        try {

            Iterator itr = AdventureWorksMeasureDetails.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                TreeNode t = new TreeNode(key);
                mRoot.addChild(t);
            }
        }
        catch (Exception e)

        {

        }
        mesRoot.addChild(mRoot);
        return  mesRoot;
    }
    /***
     * Displays the records in Expandable List adapter and List view
     * @param activityObj
     * @param btn
     */
    private void _populateJSONResponseToUI(Activity activityObj, Button btn) {

        //TextView errorView = (TextView) findViewById(R.id.ErrorText);
        //ListView mView = (ListView) findViewById(R.id.measurelistView);
        //ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        try
        {
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(activityObj,android.R.layout.simple_list_item_1, AdventureWorksMeasureDetails ); // mView.setAdapter(arrayAdapter);
           // mView.setAdapter(arrayAdapter);
            final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(activityObj,groupList,AdventureWorksDomainDetails);
            //TextView tView =  (TextView) findViewById(R.id.textView);
            btn.setVisibility(View.GONE);
            //errorView.setVisibility(View.GONE);
            //tView.setVisibility(View.GONE);
            //expListView.setAdapter(expListAdapter);

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                     String selectedChild = (String) expListAdapter.getChild(
                            groupPosition, childPosition);
                     String selectedGroup = (String) expListAdapter.getGroup(groupPosition);
                    /*Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                            .show();*/
                    String hierarchy = selectedGroup+"."+selectedChild;
                    try {
                        _startServiceThreadforHierarchy(hierarchy);
                        ParseJSONResponse parse = new ParseJSONResponse();
                        ArrayList<String> AdventureWorksHierarchyDetails = parse.ParseHierarchy(ServiceCallThread.Hierarchy);
                        //expListAdapter.setHierarchy(AdventureWorksHierarchyDetails);

                    }
                    catch (Exception e)
                    {

                    }
                    return true;
                }
            });
        }
        catch(Exception ex)
        {
           // errorView.setVisibility(View.VISIBLE);
            //errorView.setText("Error!Please Try Again.");
        }
    }
private  void _startServiceThreadforHierarchy(String param)
{
     //Initialize domain and measure as START
    //After valid population of both the variables, thread will come out of sleep mode

    ServiceCallThread.Hierarchy = "START";
    ServiceCallThread sthread = new ServiceCallThread(param);
    try {
        sthread.join();
        sthread.start();
        while (ServiceCallThread.Hierarchy == "START" ) {
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }
        }
    } catch (Exception e) {

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
    private class SimpleArrayAdapter extends ArrayAdapter<String> {
        public SimpleArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
