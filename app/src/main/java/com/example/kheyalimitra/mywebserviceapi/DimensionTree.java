package com.example.kheyalimitra.mywebserviceapi;

/**
 * Created by KheyaliMitra on 3/25/2015.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class DimensionTree extends Fragment{

    private TextView dimNode;
    private TextView mesNode;
    private  AndroidTreeView mView;
    private AndroidTreeView tView;
    private int counter = 0;
    private ArrayList<String> SelectedDimensions;
    private ArrayList<String> SelectedMeasures;
    private class SimpleArrayAdapter extends ArrayAdapter<String> {
        public SimpleArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            try {

                MainActivity main =  new MainActivity();
                String child = (String) node.getValue();
                TreeNode n = node.getRoot();
                TreeNode p = node.getParent();
                String root = (String)n.getValue();
                String parentNode = (String) p.getValue();
                String param="";
                if(root.equals("D Root")) {
                    dimNode.setText("Selected: " + "." + parentNode + "." + child);

                    if( !parentNode.equals("Dimension/Hierarchy:")&& !parentNode.equals("D Root")) {// if reparation is ro be avoided for multiple service call use (level==3 && node.getChildren().size()<1))
                        param=parentNode+"."+child;
                        SelectedDimensions.add(parentNode+"."+child);
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
                else
                {
                    if(root.equals("M Root"))
                    {
                        mesNode.setText("Selected: " + "."+parentNode + "." + child);
                        SelectedMeasures.add(parentNode+"."+child);
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
        final LayoutInflater innerInfl = inflater;
        //Get All layout from treeview.xml
        View rootView = inflater.inflate(R.layout.treeview, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.DimensionListView);
        dimNode = (TextView) rootView.findViewById(R.id.DimensionTreeNodeNode);
        mesNode = (TextView) rootView.findViewById(R.id.MeasureListNode);
        ViewGroup measureContainer = (ViewGroup) rootView.findViewById(R.id.MeasureListView);
        final Button analyzeBtn = (Button)rootView.findViewById(R.id.AnalyzeButton);

        try {
            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _processselectedquery(innerInfl,container);
                }

            };
            analyzeBtn.setOnClickListener(buttonListener);
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

            //tView.setDefaultViewHolder(IconTreeItemHolder.class);
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
    final TextView tv = (TextView)rootView.findViewById(R.id.TableViewText);
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
                                                        tv.setText("Clicked!");

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
