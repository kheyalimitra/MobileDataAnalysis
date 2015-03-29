package com.example.kheyalimitra.mywebserviceapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MainActivity  extends ActionBarActivity { //extends Activity{

    public static Context MainContext;
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

        //List view entry for first screen
        final LinkedHashMap<String, Class<?>> listItems = new LinkedHashMap<>();
        //First screen Output for List window
        listItems.put("Get Dimensions and Measures", DimensionTree.class);

        ///Display data in Text view and List View
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        final Activity activityObj = this;
        MainContext = activityObj;
        //Put list into string list
        final List<String> list = new ArrayList(listItems.keySet());

        //Activity control in first screens ActivityMain.xml
        final ListView listview = (ListView) findViewById(R.id.listview);

        //Initialize array Adpater
        final SimpleArrayAdapter adapter = new SimpleArrayAdapter(this, list);
        //Sets Adapter
        listview.setAdapter(adapter);

        //Onclick of List view
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            //btn.setOnClickListener(new View.OnClickListener() {

                                            /**
                                             * Populates JSON data into view
                                             */
                                            @Override
                                            // public void onClick(View arg0) {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try {
                                                    Intent i = new Intent(MainActivity.this, TreeViewActivity.class);
                                                    Class<?> clazz = listItems.values().toArray(new Class<?>[]{})[position];
                                                    i.putExtra(TreeViewActivity.View_PARAM, clazz);
                                                    MainActivity.this.startActivity(i);
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
    public void StartServiceThread() {

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

    /**
     * Populates top level hierarchy of domain
     *
     * @return
     */
    //private ArrayList<String> _populateTopLevelHierarchy()
    public TreeNode PopulateTreeHierarchy() {
        TreeNode dRoot = new TreeNode("Dimension/Hierarchy:");
        TreeNode dimRoot = new TreeNode("D Root");

        try {

            ArrayList<String> list = new ArrayList<String>();
            Map<String, List<String>> domainDetails = AdventureWorksDomainDetails;
            /// populate tree nodes
            Iterator it = AdventureWorksDomainDetails.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TreeNode t = new TreeNode(pair.getKey());
                if (AdventureWorksDomainDetails.get(pair.getKey()).size() > 1) {
                    List<String> innerchild = (List<String>) pair.getValue();
                    Iterator inner = innerchild.iterator();
                    while (inner.hasNext()) {
                        String child = (String) inner.next();
                        if(child.contains("."))
                        {
                            int pos = child.indexOf(".");
                            child=child.substring(pos+1);
                        }
                        TreeNode c = new TreeNode(child);
                        t.addChild(c);

                    }
                }
                dRoot.addChild(t);
            }

        } catch (Exception e) {
            String s = e.getMessage();
        }
        dimRoot.addChild(dRoot);
        return dimRoot;
    }

    public TreeNode PopulateMeasures() {
        TreeNode mRoot = new TreeNode("Measures:");
        TreeNode mesRoot = new TreeNode("M Root");
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
        return mesRoot;
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
        try {
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(activityObj, android.R.layout.simple_list_item_1, AdventureWorksMeasureDetails); // mView.setAdapter(arrayAdapter);
            // mView.setAdapter(arrayAdapter);
            final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(activityObj, groupList, AdventureWorksDomainDetails);
            //TextView tView =  (TextView) findViewById(R.id.textView);
            //btn.setVisibility(View.GONE);
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
                    String hierarchy = selectedGroup + "." + selectedChild;
                    try {
                        //_startServiceThreadforHierarchy(hierarchy);
                        ParseJSONResponse parse = new ParseJSONResponse();
                        ArrayList<String> AdventureWorksHierarchyDetails = parse.ParseHierarchy(ServiceCallThread.Hierarchy);
                        //expListAdapter.setHierarchy(AdventureWorksHierarchyDetails);

                    } catch (Exception e) {

                    }
                    return true;
                }
            });
        } catch (Exception ex) {
            // errorView.setVisibility(View.VISIBLE);
            //errorView.setText("Error!Please Try Again.");
        }
    }

public  void StartServiceThreadforHierarchy(String param)
{
     //Initialize domain and measure as START
    //After valid population of both the variables, thread will come out of sleep mode

    ServiceCallThread.Hierarchy = "START";

    ServiceCallThread sthread = new ServiceCallThread(param);
    try {
        sthread.join();
        sthread.start();
        while (ServiceCallThread.Hierarchy == "START") {
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }
        }
    } catch (Exception e) {

    }
}
public void StartServiceThreadForUserQuery(String query)
{
    ServiceCallThread.UserQueryResponse = "START";

    ServiceCallThread sthread = new ServiceCallThread(query);
    try {
        sthread.join();
        sthread.start();
        while (ServiceCallThread.UserQueryResponse == "START") {
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

    //Class for
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
