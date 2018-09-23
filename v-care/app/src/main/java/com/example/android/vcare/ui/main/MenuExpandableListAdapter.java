package com.example.android.vcare.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vcare.R;
import com.example.android.vcare.util.MenuUtil;

import java.util.HashMap;
import java.util.List;

public class MenuExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Integer> expandableListTitle;
    private HashMap<Integer, List<Integer>> expandableListDetail;

    public MenuExpandableListAdapter(Context context, List<Integer> expandableListTitle,
                                     HashMap<Integer, List<Integer>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    public int getChildMenuId(int listPosition, int expandedListPosition) {
        return (Integer) getChild(listPosition, expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = MenuUtil.getMenuNameFromObject(context, getChild(listPosition, expandedListPosition));
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_menu_child_item, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.title);
        childText.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    public int getGroupMenuId(int listPosition) {
        return (Integer) getGroup(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = MenuUtil.getMenuNameFromObject(context, getGroup(listPosition));
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_menu_parent_item, null);
        }
        TextView parentText = (TextView) convertView.findViewById(R.id.title);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        if (getChildrenCount(listPosition) > 0) {
            if (isExpanded) {
                indicator.setImageResource(R.drawable.ic_arrow_drop_up);
            } else {
                indicator.setImageResource(R.drawable.ic_arrow_drop_down);
            }
        }
        parentText.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
