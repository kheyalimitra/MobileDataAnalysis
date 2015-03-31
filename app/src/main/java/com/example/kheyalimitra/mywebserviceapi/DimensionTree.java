package com.example.kheyalimitra.mywebserviceapi;

/**
 * Created by KheyaliMitra on 3/25/2015.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DimensionTree extends Fragment{

    private TextView dimNode;
    private TextView mesNode;
    private  AndroidTreeView mView;
    private AndroidTreeView tView;
    private int listMenuPos_Dimen = 0;
    private int listMenuPos_Mes = 0;
    private List<String> queryList ;
    public static List<Map<String,String>>QueryResponse;
    private ArrayList<String> SelectedDimensions;
    private ArrayList<String> SelectedMeasures;
    private class SimpleArrayAdapter extends ArrayAdapter<String> {
        public SimpleArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @Override
        public boolean isEnabled(int position) {
            if(position == listMenuPos_Dimen || position == listMenuPos_Mes) {
                return false;
            }
            return true;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            MainActivity main =  new MainActivity();
            try {
                String child = (String) node.getValue();
                TreeNode n = node.getRoot();
                TreeNode p = node.getParent();
                TreeNode grandP = p.getParent();
                String root = (String)n.getValue();
                String parentNode = (String) p.getValue();
                String grandParentNode="";
                if(grandP !=null)
                    grandParentNode= (String)grandP.getValue();
                String param="";
                if(root.equals("D Root")) {

                    dimNode.setText("Selected: "+grandParentNode + "." + parentNode + "." + child);

                    if( !parentNode.equals("Dimension/Hierarchy:")&& !parentNode.equals("D Root")) {// if reparation is ro be avoided for multiple service call use (level==3 && node.getChildren().size()<1))
                        param=parentNode+"."+child;
                        if(grandParentNode.equals("D Root")) {
                            SelectedDimensions.add( child);
                        }
                            else {
                                if(grandParentNode.equals("Dimension/Hierarchy:"))
                                    SelectedDimensions.add(parentNode + "." + child);
                            else
                                    SelectedDimensions.add(grandParentNode+"."+parentNode + "." + child);
                            }
                        List<TreeNode> children = node.getChildren();
                        if(children.size()==0 && node.getLevel()==3) {
                            main.StartServiceThreadforHierarchy(param);
                            ParseJSONResponse parse = new ParseJSONResponse();
                            ArrayList<String> AdventureWorksHierarchyDetails = parse.ParseHierarchy(ServiceCallThread.Hierarchy);
                            try {

                                Iterator itr = AdventureWorksHierarchyDetails.iterator();
                                while (itr.hasNext()) {
                                    String key = (String) itr.next();
                                    TreeNode t = new TreeNode(key);
                                    node.addChild(t);
                                }
                            } catch (Exception e)

                            {

                            }
                        }
                    }

                }
                else
                {
                    if(root.equals("M Root"))
                    {
                        mesNode.setText("Selected: " + child);
                        if(!child.equals("Measures:") && !child.equals("M Root") )
                        SelectedMeasures.add(child);
                    }


                }
            } catch (Exception e) {
                String s;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SelectedDimensions =  new ArrayList<String>();
        SelectedMeasures = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        //Call service and parse JSON
        ParseJSONResponse parse = new ParseJSONResponse();
        MainActivity main = new MainActivity();
        //Start Service Thread
        main.StartServiceThread();

        //Get All layout from treeview.xml
        View rootView = inflater.inflate(R.layout.treeview, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.DimensionListView);
        dimNode = (TextView) rootView.findViewById(R.id.DimensionTreeNodeNode);
        mesNode = (TextView) rootView.findViewById(R.id.MeasureListNode);
        final ViewGroup measureContainer = (ViewGroup) rootView.findViewById(R.id.MeasureListView);
        final Button analyzeBtn = (Button)rootView.findViewById(R.id.AnalyzeButton);
        final Button execBtn = (Button)rootView.findViewById(R.id.executeButton);
        final ListView selectedQuery = (ListView)rootView.findViewById(R.id.queryView);
        final TextView finalSelection =(TextView)rootView.findViewById(R.id.finalSelections);
        execBtn.setVisibility(View.INVISIBLE);
        selectedQuery.setVisibility(View.INVISIBLE);
        finalSelection.setVisibility(View.INVISIBLE);
        try {

            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    containerView.setVisibility(View.INVISIBLE);
                    measureContainer.setVisibility(View.INVISIBLE);
                    dimNode.setVisibility(View.INVISIBLE);
                    mesNode.setVisibility(View.INVISIBLE);
                    execBtn.setVisibility(View.VISIBLE);
                    selectedQuery.setVisibility(View.VISIBLE);
                    finalSelection.setVisibility(View.VISIBLE);
                    final LinkedHashMap<String, String> listItems = new LinkedHashMap<>();

                    listItems.put("First","Selected Dimensions are given below:");

                    for( int i=0;i<SelectedDimensions.size();i++) {
                        listItems.put(SelectedDimensions.get(i), SelectedDimensions.get(i));
                    }

                    listItems.put("Second","Selected Measures are given below:");

                    for( int i=0;i<SelectedMeasures.size();i++)
                        listItems.put(SelectedMeasures.get(i), SelectedMeasures.get(i));
                    List<String> list = new ArrayList(listItems.values());
                    listMenuPos_Mes = list.lastIndexOf("Selected Measures are given below:");
                    listMenuPos_Dimen = list.lastIndexOf("Selected Dimensions are given below:");
                    queryList = list;
                    SimpleArrayAdapter adapter = new SimpleArrayAdapter(MainActivity.MainContext, list);
                    //Sets Adapter
                    selectedQuery.setAdapter(adapter);

                    //Onclick of List view
                    selectedQuery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                               @Override

                                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                   try {
                                                                       queryList=null;
                                                                       int i= position;
                                                                       String itemSelected = (String) (selectedQuery.getItemAtPosition(position));
                                                                       listItems.remove(itemSelected);
                                                                       List<String> l = new ArrayList(listItems.values());
                                                                       queryList=l;
                                                                       // set both the items non clickable using adapter
                                                                       listMenuPos_Mes = l.lastIndexOf("Selected Measures are given below:");
                                                                       listMenuPos_Dimen = l.lastIndexOf("Selected Dimensions are given below:");
                                                                       SimpleArrayAdapter a = new SimpleArrayAdapter(MainActivity.MainContext, l);
                                                                       //Sets Adapter
                                                                       selectedQuery.setAdapter(a);
                                                                       Toast.makeText(MainActivity.MainContext, itemSelected+"is removed.", Toast.LENGTH_LONG).show();
                                                                   } catch (Exception e) {

                                                                   }
                                                               }
                                                           }
                    );

                }

            };
            analyzeBtn.setOnClickListener(buttonListener);
            View.OnClickListener executeListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    boolean whereClause =false;
                    int axisCount=0;
                    if(queryList.size()<= 5) {
                        String query = "Select\n" ;
                        for (int i = 0; i < queryList.size(); i++) {
                            if (!queryList.get(i).equals("Selected Dimensions are given below:")) {
                                if (!whereClause && !queryList.get(i).equals("Selected Measures are given below:")) {
                                    if (axisCount < 2) {
                                        int pos = queryList.get(i).indexOf(".");
                                        String firstPart = queryList.get(i).substring(0, pos);
                                        String secondPart = queryList.get(i).substring(pos + 1);
                                        query += "{[" + firstPart + "].[";
                                        String rest;
                                        if (secondPart.contains(".")) {
                                            pos = secondPart.indexOf(".");
                                            rest = secondPart.substring(pos + 1);
                                            secondPart = secondPart.substring(0, pos);
                                            query += secondPart + "].[" + rest + "].children} ON " + axisCount++ + ",\n";
                                        } else
                                            query += secondPart + "].children} ON " + axisCount++ + "\n";
                                    }

                                } else {
                                    if (queryList.get(i).equals("Selected Measures are given below:")) {
                                        whereClause = true;
                                        int commaPos = query.lastIndexOf(",");
                                        query = query.substring(0, commaPos) + "\n";
                                        query += " \n" +
                                                "from [adventure works]\n" +
                                                "where\n";
                                    } else
                                        query += "[measures].[" + queryList.get(i) + "]\n";
                                }
                            }
                        }
                        // check if query string is containg atleast 1 dimension and 1 measure
                        if(query.contains("measures")&& query.contains("ON ")) {
                            //int commaPos = query.lastIndexOf(",");
                            //query = query.substring(0, commaPos) + "\n";
                            MainActivity main = new MainActivity();
                            try {
                                main.StartServiceThreadForUserQuery(query);
                                //ParseJSONResponse parse = new ParseJSONResponse();
                                //QueryResponse = parse.ParseUserQuery(ServiceCallThread.UserQueryResponse);
                                GoogleImageGraphActivity gIGA = new GoogleImageGraphActivity();
                                Intent intent = new Intent(MainActivity.MainContext, GoogleImageGraphActivity.class);
                                DimensionTree.this.startActivity(intent);
                            } catch (Exception e) {
                                String s = e.getMessage();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.MainContext, "At least 1 dimension and 1 measure must be selected. Please retry.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.MainContext, "At most 2 dimensions and 1 measure can be selected. Please retry.", Toast.LENGTH_LONG).show();
                    }

                }


            };
            execBtn.setOnClickListener(executeListener);
            //Parse Domain List
            main.AdventureWorksDomainDetails = parse.ParseDomainRecords(ServiceCallThread.Domains);
            //Parse Measure LIst
            main.AdventureWorksMeasureDetails = parse.ParseMeasureResponse(ServiceCallThread.Measures);
            //Populate Dimension List view
            TreeNode root = main.PopulateTreeHierarchy();
            IconTreeItem nodeItem = new IconTreeItem(0, "Dimension/Hierarchy:");

            root.setViewHolder(new IconTreeItemHolder(main.MainContext));
            root.setClickListener(nodeClickListener);

            tView = new AndroidTreeView(getActivity(), root);
            tView.setDefaultAnimation(true);
            tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
            tView.setDefaultNodeClickListener(nodeClickListener);
            containerView.addView(tView.getView());
            tView.collapseAll();

            if (savedInstanceState != null) {
                String state = savedInstanceState.getString("tState");
                if (!TextUtils.isEmpty(state)) {
                    tView.restoreState(state);
                }
            }

            TreeNode measuresRoot = main.PopulateMeasures();
            IconTreeItem mesItem = nodeItem = new IconTreeItem(1, "Measures");
            measuresRoot.setViewHolder(new IconTreeItemHolder(main.MainContext));
            measuresRoot.setClickListener(nodeClickListener);
            mView = new AndroidTreeView(getActivity(), measuresRoot);
            mView.setDefaultAnimation(true);
            mView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
            mView.setDefaultNodeClickListener(nodeClickListener);
            measureContainer.addView(mView.getView());
            if (savedInstanceState != null) {
                String state = savedInstanceState.getString("tState");
                if (!TextUtils.isEmpty(state)) {
                    mView.restoreState(state);
                }
            }
            mView.collapseAll();
        } catch (Exception e) {
            String s = e.getMessage();
        }
        return rootView;
    }

private  void _processselectedquery(LayoutInflater inflater, final ViewGroup container)
{

    View rootView = inflater.inflate(R.layout.display, null, false);
    //List view entry for first screen
    final LinkedHashMap<String, ArrayList<String>> listItems = new LinkedHashMap<>();
    listItems.put("Selected Dimensions", SelectedDimensions);
    listItems.put("Selected Measures", SelectedMeasures);
    final List<String> list = new ArrayList(listItems.keySet());
   // final TextView tv = (TextView)rootView.findViewById(R.id.TableViewText);
    ListView selectedQueries = (ListView) rootView.findViewById(R.id.queryView);
    final SimpleArrayAdapter adapter = new SimpleArrayAdapter(MainActivity.MainContext, list);
    //Sets Adapter
    selectedQueries.setAdapter(adapter);

    //Onclick of List view
    selectedQueries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               // public void onClick(View arg0) {
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                   try {
                                                        ///
     //                                                   tv.setText("Clicked!");

                                                   } catch (Exception e) {

                                                   }
                                               }
                                           }
        );
   }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.example.kheyalimitra.mywebserviceapi.R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
/*
    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItem(R.string.ic_folder, "Downloads" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.example.kheyalimitra.mywebserviceapi.R.id.expandAll:
                tView.expandAll();
                break;

            case com.example.kheyalimitra.mywebserviceapi.R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
