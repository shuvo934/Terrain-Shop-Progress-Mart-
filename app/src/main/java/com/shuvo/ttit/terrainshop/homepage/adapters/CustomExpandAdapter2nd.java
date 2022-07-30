package com.shuvo.ttit.terrainshop.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.lists.CategoryListForNavItem;

import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.homepage.HomePage.expandableListView;
import static com.shuvo.ttit.terrainshop.homepage.HomePage.fromPicture;


public class CustomExpandAdapter2nd extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CategoryListForNavItem> categoryLists;
    //private Map<String,List<String>> listItem;


    public CustomExpandAdapter2nd(Context context, ArrayList<CategoryListForNavItem> categoryLists) {
        this.context = context;
        this.categoryLists = categoryLists;
    }

    @Override
    public int getGroupCount() {
        return categoryLists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryLists.get(i).getSubCategoryLists().size();
    }

    @Override
    public Object getGroup(int i) {
        return categoryLists.get(i).getCategoryName();
    }

    @Override
    public Object getChild(int i, int i1) {
        return categoryLists.get(i).getSubCategoryLists().get(i1).getIem_name();
    }

    @Override
    public long getGroupId(int i) {
        return Integer.parseInt(categoryLists.get(i).getIem_id());
    }

    @Override
    public long getChildId(int i, int i1) {
        return Integer.parseInt(categoryLists.get(i).getSubCategoryLists().get(i1).getIem_id());
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String  title = (String) getGroup(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_group,null);
        }
        TextView textView = view.findViewById(R.id.listTitle);
        textView.setText(title);
        ImageView imageView = view.findViewById(R.id.arrow_of_group);
        if (categoryLists.get(i).getSubCategoryLists().size() == 0) {
            imageView.setVisibility(View.GONE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            if (expandableListView.isGroupExpanded(i)) {
                imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            }else {
                imageView.setImageResource(R.drawable.ic_baseline_arrow_right_24);
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromPicture = true;
                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                    imageView.setImageResource(R.drawable.ic_baseline_arrow_right_24);
                }
                else {
                    expandableListView.expandGroup(i);
                    imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }


            }
        });

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String  title = (String) getChild(i,i1);
        title = "  " + title;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        }
        TextView textView = view.findViewById(R.id.expandabledListItem);
        textView.setText(title);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
