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
import android.widget.TextView;
import android.content.Context;

import com.unnamed.b.atv.model.TreeNode;

import com.unnamed.b.atv.view.AndroidTreeView;

public class DimensionTree extends Fragment{

    private TextView dimNode;
    private TextView mesNode;
    private  AndroidTreeView mView;
    private AndroidTreeView tView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ParseJSONResponse parse = new ParseJSONResponse();
        MainActivity main = new MainActivity();
        main.StartServiceThread();

        View rootView = inflater.inflate(R.layout.treeview, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        dimNode = (TextView) rootView.findViewById(R.id.selectedNode);
        mesNode =(TextView) rootView.findViewById(R.id.MeasureNode);
        ViewGroup measureContainer =(ViewGroup) rootView.findViewById(R.id.container2);


        try {
            main.AdventureWorksDomainDetails = parse.ParseDomainRecords(ServiceCallThread.Domains);
            main.AdventureWorksMeasureDetails = parse.ParseMeasureResponse(ServiceCallThread.Measures);
          TreeNode root = main.PopulateTreeHierarchy();
          IconTreeItem nodeItem = new IconTreeItem(0,"Dimension/Hierarchy:");
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

            TreeNode measuresRoot =  main.PopulateMeasures();
            IconTreeItem mesItem = nodeItem = new IconTreeItem(1,"Measures");
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
    }
    catch (Exception e)
    {
        String s = e.getMessage();
    }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.example.kheyalimitra.mywebserviceapi.R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

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

    private int counter = 0;
/*
    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItem(R.string.ic_folder, "Downloads" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }
*/
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            try {

                String  child =(String) node.getValue();
                //String parent =node.getPath();
                TreeNode p =  node.getParent();
                dimNode.setText("Selected: " + (String)p.getValue()+"."+child);
            }
            catch(Exception e)
            {
                String s;
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
