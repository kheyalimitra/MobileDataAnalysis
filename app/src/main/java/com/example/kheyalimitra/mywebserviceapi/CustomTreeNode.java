package com.example.kheyalimitra.mywebserviceapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by KheyaliMitra on 3/25/2015.
 */

public class CustomTreeNode extends TreeNode.BaseNodeViewHolder<IconTreeItem> {

    public CustomTreeNode(Context context) {
        super(context);
    }

    @Override
    public View createNodeView (TreeNode treeNode, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.abc_activity_chooser_view, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.textView);
        tvValue.setText(value.text);

        return view;
    }



}
