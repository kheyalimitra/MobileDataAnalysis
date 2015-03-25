package com.example.kheyalimitra.mywebserviceapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by KheyaliMitra on 3/9/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    public Map<String, List<String>> AdvWorkDetails;
    public List<String> dimentions;
    public List<String> hierarchy;
    public ExpandableListAdapter(Activity context,List<String> dimentions,
                                 Map<String, List<String>> AdvWorkDetails) {
        this.context = context;
        this.AdvWorkDetails = AdvWorkDetails;
        this.dimentions = dimentions;

    }

    /* public ExpandableListAdapter( List<String> dimentions,
                                 Map<String, List<String>> AdvWorkDetails) {
        this.AdvWorkDetails = AdvWorkDetails;
        this.dimentions = dimentions;
    }*/
    public Object getChild(int groupPosition, int childPosition) {
        return AdvWorkDetails.get(dimentions.get(groupPosition)).get(childPosition);
    }
    public Object getSubChild( int childPosition) {
        return hierarchy.get(childPosition);
    }
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String Children = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_items, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.DomainHierarchy);
        ImageView fetchChildren = (ImageView) convertView.findViewById(R.id.fetchChildren);
        fetchChildren.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        AdvWorkDetails.get(dimentions.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/
            }
        });

        item.setText(Children);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return AdvWorkDetails.get(dimentions.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return dimentions.get(groupPosition);
    }

    public int getGroupCount() {
        return dimentions.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupNames = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_items,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.DomainHierarchy);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(groupNames);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
