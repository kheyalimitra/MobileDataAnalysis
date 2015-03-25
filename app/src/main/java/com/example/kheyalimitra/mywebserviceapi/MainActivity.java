package com.example.kheyalimitra.mywebserviceapi;

import android.app.AlertDialog;
import android.app.Service;
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
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity  extends Activity{

    //Top level names of Domain
    List<String> groupList;

    //Child hiearchy
    public Map<String, List<String>> AdventureWorksDomainDetails;

    //Measure details
    public ArrayList<String> AdventureWorksMeasureDetails ;

    //List view object
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.getbtn);

        //final TextView dimView = (TextView) findViewById(R.id.domainView);
        //final TextView measureView = (TextView) findViewById(R.id.measureView);
        //dimView.setVisibility(View.INVISIBLE);
        //measureView.setVisibility(View.INVISIBLE);
        ///Display data in Text view and List View
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        final Activity activityObj = this;
        //PopulateDetails details =  new PopulateDetails();

        //details.BindButtonClick(btn, expListAdapter, activityObj);

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
                                           _populateJSONResponseToUI(activityObj, btn);


                                           TreeNode root = TreeNode.root();
                                           TreeNode parent = new TreeNode("MyParentNode");
                                           TreeNode child0 = new TreeNode("ChildNode0");
                                           TreeNode child1 = new TreeNode("ChildNode1");
                                           TreeNode child01 = new TreeNode("ChildNode01");
                                           TreeNode child12 = new TreeNode("ChildNode12");
                                           parent.addChildren(child0, child1);
                                           child0.addChild(child01);
                                           child1.addChild(child12);
                                           root.addChild(parent);
                                           AndroidTreeView tView = new AndroidTreeView(activityObj,root);
                                           ScrollView sv =  (ScrollView)findViewById(R.id.scrollView);
                                           sv.addView(tView.getView());
                                           IconTreeItem nodeItem = new IconTreeItem();
                                           TreeNode child= new TreeNode(nodeItem).setViewHolder(new CustomTreeNode(activityObj));

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
    private ArrayList<String> _populateTopLevelHierarchy()
    {
        ArrayList<String> list  =new ArrayList<String>();
        Map<String,List<String>> domainDetails = AdventureWorksDomainDetails;
        Set<String> keys= AdventureWorksDomainDetails.keySet();
        list.addAll(keys);
        Collections.sort(list);
        return  list;
    }

    /***
     * Displays the records in Expandable List adapter and List view
     * @param activityObj
     * @param btn
     */
    private void _populateJSONResponseToUI(Activity activityObj, Button btn) {

        TextView errorView = (TextView) findViewById(R.id.ErrorText);
        ListView mView = (ListView) findViewById(R.id.measurelistView);
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        try
        {
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(activityObj,android.R.layout.simple_list_item_1, AdventureWorksMeasureDetails ); // mView.setAdapter(arrayAdapter);
            mView.setAdapter(arrayAdapter);
            final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(activityObj,groupList,AdventureWorksDomainDetails);
            TextView tView =  (TextView) findViewById(R.id.textView);
            btn.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            tView.setVisibility(View.GONE);
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
            errorView.setVisibility(View.VISIBLE);
            errorView.setText("Error!Please Try Again.");
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
}
