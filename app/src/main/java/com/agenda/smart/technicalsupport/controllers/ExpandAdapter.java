package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.Category;
import com.agenda.smart.technicalsupport.models.ExpandModel;
import com.agenda.smart.technicalsupport.views.ProblemsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Abdelrahman on 10/10/2018.
 */

//lazem ykon 2 list gwa b3d
public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private ExpandModel expandModel;
    RecyclerView clientRecyclerView;
    GridLayoutManager layoutManager;
    ClientRecyclerAdapter clientRecyclerAdapter;

    //dataSaver = getDefaultSharedPreferences(mcontext);


    public ExpandAdapter(Context context, ExpandModel expandModel) {
        this.mcontext = context;
        this.expandModel = expandModel;


    }

    @Override
    public int getGroupCount() {
        return expandModel.getCategories().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return expandModel.getCategories().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return expandModel.getCategories().get(i).getClients().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.client_title, null);
        }
        TextView name = (TextView) view.findViewById(R.id.client_title);

        name.setText(expandModel.getCategories().get(i).getName());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.client_images, null);
        }

        clientRecyclerView = view.findViewById(R.id.recycler_client);
        layoutManager = new GridLayoutManager(mcontext, 3);
        clientRecyclerView.setLayoutManager(layoutManager);
        clientRecyclerAdapter = new ClientRecyclerAdapter(mcontext,expandModel.getCategories().get(i).getClients());
        clientRecyclerView.setAdapter(clientRecyclerAdapter);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
